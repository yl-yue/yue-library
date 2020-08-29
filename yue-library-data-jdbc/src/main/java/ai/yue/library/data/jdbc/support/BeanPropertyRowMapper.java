package ai.yue.library.data.jdbc.support;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.util.BeanUtils;
import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 增强 {@linkplain org.springframework.jdbc.core.BeanPropertyRowMapper}，支持 {@linkplain FieldName} 注解
 * 
 * @author	ylyue
 * @since	2020年2月24日
 */
public class BeanPropertyRowMapper<T> implements RowMapper<T> {

	/** Logger available to subclasses. */
	protected final Log logger = LogFactory.getLog(getClass());

	/** The class we are mapping to. */
	@Nullable
	private Class<T> mappedClass;

	/** Whether we're strictly validating. */
	private boolean checkFullyPopulated = false;

	/** Whether we're defaulting primitives when mapping a null value. */
	private boolean primitivesDefaultedForNullValue = false;

	/** ConversionService for binding JDBC values to bean properties. */
	@Nullable
	private ConversionService conversionService = DefaultConversionService.getSharedInstance();

	/** Map of the fields we provide mapping for. */
	@Nullable
	private Map<String, PropertyDescriptor> mappedFields;

	/** Set of bean properties we provide mapping for. */
	@Nullable
	private Set<String> mappedProperties;


	/**
	 * Create a new {@code BeanPropertyRowMapper} for bean-style configuration.
	 * @see #setMappedClass
	 * @see #setCheckFullyPopulated
	 */
	public BeanPropertyRowMapper() {
	}

	/**
	 * Create a new {@code BeanPropertyRowMapper}, accepting unpopulated
	 * properties in the target bean.
	 * <p>Consider using the {@link #newInstance} factory method instead,
	 * which allows for specifying the mapped type once only.
	 * @param mappedClass the class that each row should be mapped to
	 */
	public BeanPropertyRowMapper(Class<T> mappedClass) {
		initialize(mappedClass);
	}

	/**
	 * Create a new {@code BeanPropertyRowMapper}.
	 * @param mappedClass the class that each row should be mapped to
	 * @param checkFullyPopulated whether we're strictly validating that
	 * all bean properties have been mapped from corresponding database fields
	 */
	public BeanPropertyRowMapper(Class<T> mappedClass, boolean checkFullyPopulated) {
		initialize(mappedClass);
		this.checkFullyPopulated = checkFullyPopulated;
	}


	/**
	 * Set the class that each row should be mapped to.
	 */
	public void setMappedClass(Class<T> mappedClass) {
		if (this.mappedClass == null) {
			initialize(mappedClass);
		}
		else {
			if (this.mappedClass != mappedClass) {
				throw new InvalidDataAccessApiUsageException("The mapped class can not be reassigned to map to " +
						mappedClass + " since it is already providing mapping for " + this.mappedClass);
			}
		}
	}

	/**
	 * Get the class that we are mapping to.
	 */
	@Nullable
	public final Class<T> getMappedClass() {
		return this.mappedClass;
	}

	/**
	 * Set whether we're strictly validating that all bean properties have been mapped
	 * from corresponding database fields.
	 * <p>Default is {@code false}, accepting unpopulated properties in the target bean.
	 */
	public void setCheckFullyPopulated(boolean checkFullyPopulated) {
		this.checkFullyPopulated = checkFullyPopulated;
	}

	/**
	 * Return whether we're strictly validating that all bean properties have been
	 * mapped from corresponding database fields.
	 */
	public boolean isCheckFullyPopulated() {
		return this.checkFullyPopulated;
	}

	/**
	 * Set whether we're defaulting Java primitives in the case of mapping a null value
	 * from corresponding database fields.
	 * <p>Default is {@code false}, throwing an exception when nulls are mapped to Java primitives.
	 */
	public void setPrimitivesDefaultedForNullValue(boolean primitivesDefaultedForNullValue) {
		this.primitivesDefaultedForNullValue = primitivesDefaultedForNullValue;
	}

	/**
	 * Return whether we're defaulting Java primitives in the case of mapping a null value
	 * from corresponding database fields.
	 */
	public boolean isPrimitivesDefaultedForNullValue() {
		return this.primitivesDefaultedForNullValue;
	}

	/**
	 * Set a {@link ConversionService} for binding JDBC values to bean properties,
	 * or {@code null} for none.
	 * <p>Default is a {@link DefaultConversionService}, as of Spring 4.3. This
	 * provides support for {@code java.time} conversion and other special types.
	 * @since 4.3
	 * @see #initBeanWrapper(BeanWrapper)
	 */
	public void setConversionService(@Nullable ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	/**
	 * Return a {@link ConversionService} for binding JDBC values to bean properties,
	 * or {@code null} if none.
	 * @since 4.3
	 */
	@Nullable
	public ConversionService getConversionService() {
		return this.conversionService;
	}


	/**
	 * Initialize the mapping meta-data for the given class.
	 * @param mappedClass the mapped class
	 */
	protected void initialize(Class<T> mappedClass) {
		this.mappedClass = mappedClass;
		this.mappedFields = new HashMap<>();
		this.mappedProperties = new HashSet<>();
		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(mappedClass);
		for (PropertyDescriptor pd : pds) {
			Method writeMethod = pd.getWriteMethod();
			if (writeMethod == null) {
				writeMethod = ReflectUtil.getMethodByName(mappedClass, BeanUtils.getSetMethodName(pd.getName()));
			}
			
			if (writeMethod != null) {
				String databaseFieldName = AnnotationUtil.getAnnotationValue(ReflectUtil.getField(mappedClass, pd.getName()), FieldName.class);
				if (StrUtil.isNotEmpty(databaseFieldName)) {
					this.mappedFields.put(databaseFieldName, pd);
				} else {
					this.mappedFields.put(lowerCaseName(pd.getName()), pd);
					String underscoredName = underscoreName(pd.getName());
					if (!lowerCaseName(pd.getName()).equals(underscoredName)) {
						this.mappedFields.put(underscoredName, pd);
					}
				}
				
				this.mappedProperties.add(pd.getName());
			}
		}
	}

	/**
	 * Convert a name in camelCase to an underscored name in lower case.
	 * Any upper case letters are converted to lower case with a preceding underscore.
	 * @param name the original name
	 * @return the converted name
	 * @since 4.2
	 * @see #lowerCaseName
	 */
	protected String underscoreName(String name) {
		if (!StringUtils.hasLength(name)) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		result.append(lowerCaseName(name.substring(0, 1)));
		for (int i = 1; i < name.length(); i++) {
			String s = name.substring(i, i + 1);
			String slc = lowerCaseName(s);
			if (!s.equals(slc)) {
				result.append("_").append(slc);
			}
			else {
				result.append(s);
			}
		}
		return result.toString();
	}

	/**
	 * Convert the given name to lower case.
	 * By default, conversions will happen within the US locale.
	 * @param name the original name
	 * @return the converted name
	 * @since 4.2
	 */
	protected String lowerCaseName(String name) {
		return name.toLowerCase(Locale.US);
	}


	/**
	 * Extract the values for all columns in the current row.
	 * <p>Utilizes public setters and result set meta-data.
	 * @see java.sql.ResultSetMetaData
	 */
	@Override
	public T mapRow(ResultSet rs, int rowNumber) throws SQLException {
		Assert.state(this.mappedClass != null, "Mapped class was not specified");
		T mappedObject = org.springframework.beans.BeanUtils.instantiateClass(this.mappedClass);
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
		initBeanWrapper(bw);
		Object wrappedInstance = bw.getWrappedInstance();
		
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Set<String> populatedProperties = (isCheckFullyPopulated() ? new HashSet<>() : null);

		for (int index = 1; index <= columnCount; index++) {
			String column = JdbcUtils.lookupColumnName(rsmd, index);
			String field = lowerCaseName(StringUtils.delete(column, " "));
			PropertyDescriptor pd = (this.mappedFields != null ? this.mappedFields.get(field) : null);
			if (pd != null) {
				try {
					Object value = getColumnValue(rs, index, pd);
					if (rowNumber == 0 && logger.isDebugEnabled()) {
						logger.debug("Mapping column '" + column + "' to property '" + pd.getName() +
								"' of type '" + ClassUtils.getQualifiedName(pd.getPropertyType()) + "'");
					}
					try {
						/*
						 * 支持JavaBean中存在多个setMethod方法；
						 * 在JavaBean中调用setMethod方法设置JSONObject类型value时进行额外解析处理
						 */
						Method writeMethod = pd.getWriteMethod();
						if (writeMethod != null
								&& ((value != null && value.getClass() == pd.getPropertyType()) || value == null)) {
							bw.setPropertyValue(pd.getName(), value);
						} else {
							String setMethodName = BeanUtils.getSetMethodName(pd.getName());
							if (value != null) {
								writeMethod = ReflectUtil.getPublicMethod(this.mappedClass, setMethodName, value.getClass());
							}
							if (writeMethod == null) {
								writeMethod = ReflectUtil.getPublicMethod(this.mappedClass, setMethodName, Object.class);
							}
							if (writeMethod == null) {
								writeMethod = ReflectUtil.getPublicMethod(this.mappedClass, setMethodName, pd.getPropertyType());
								if (writeMethod != null) {
									if (Map.class.isAssignableFrom(pd.getPropertyType())) {
										value = Convert.toJSONObject(value);
									}
								}
							}
							if (writeMethod != null) {
								ReflectUtil.invokeWithCheck(wrappedInstance, writeMethod, value);
							}
						}
					} catch (TypeMismatchException ex) {
						if (value == null && this.primitivesDefaultedForNullValue) {
							if (logger.isDebugEnabled()) {
								logger.debug("Intercepted TypeMismatchException for row " + rowNumber +
										" and column '" + column + "' with null value when setting property '" +
										pd.getName() + "' of type '" +
										ClassUtils.getQualifiedName(pd.getPropertyType()) +
										"' on object: " + mappedObject, ex);
							}
						} else {
							throw ex;
						}
					}
					if (populatedProperties != null) {
						populatedProperties.add(pd.getName());
					}
				} catch (NotWritablePropertyException ex) {
					throw new DataRetrievalFailureException(
							"Unable to map column '" + column + "' to property '" + pd.getName() + "'", ex);
				}
			} else {
				// No PropertyDescriptor found
				if (rowNumber == 0 && logger.isDebugEnabled()) {
					logger.debug("No property found for column '" + column + "' mapped to field '" + field + "'");
				}
			}
		}

		if (populatedProperties != null && !populatedProperties.equals(this.mappedProperties)) {
			throw new InvalidDataAccessApiUsageException("Given ResultSet does not contain all fields " +
					"necessary to populate object of class [" + this.mappedClass.getName() + "]: " +
					this.mappedProperties);
		}

		return mappedObject;
	}

	/**
	 * Initialize the given BeanWrapper to be used for row mapping.
	 * To be called for each row.
	 * <p>The default implementation applies the configured {@link ConversionService},
	 * if any. Can be overridden in subclasses.
	 * @param bw the BeanWrapper to initialize
	 * @see #getConversionService()
	 * @see BeanWrapper#setConversionService
	 */
	protected void initBeanWrapper(BeanWrapper bw) {
		ConversionService cs = getConversionService();
		if (cs != null) {
			bw.setConversionService(cs);
		}
	}

	/**
	 * Retrieve a JDBC object value for the specified column.
	 * <p>The default implementation calls
	 * {@link JdbcUtils#getResultSetValue(java.sql.ResultSet, int, Class)}.
	 * Subclasses may override this to check specific value types upfront,
	 * or to post-process values return from {@code getResultSetValue}.
	 * @param rs is the ResultSet holding the data
	 * @param index is the column index
	 * @param pd the bean property that each result object is expected to match
	 * @return the Object value
	 * @throws SQLException in case of extraction failure
	 * @see org.springframework.jdbc.support.JdbcUtils#getResultSetValue(java.sql.ResultSet, int, Class)
	 */
	@Nullable
	protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
		return JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
	}


	/**
	 * Static factory method to create a new {@code BeanPropertyRowMapper}
	 * (with the mapped class specified only once).
	 * @param mappedClass the class that each row should be mapped to
	 */
	public static <T> BeanPropertyRowMapper<T> newInstance(Class<T> mappedClass) {
		return new BeanPropertyRowMapper<>(mappedClass);
	}

}
