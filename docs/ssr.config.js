module.exports = {
	template: './ssr.html',
	maxAge: 60 * 60 * 1000,
	config: {
		basePath: 'https://ylyue.cn/',
		name: 'yue-library',
		repo: 'https://gitee.com/yl-yue/yue-library',
		coverpage: true,
		// 封面作为首页
		onlyCover: true,
		// 切换页面后是否自动跳转到页面顶部
		auto2top: true,
		// 加载 _sidebar.md 文件，侧边栏
		loadSidebar: true,
		// 加载 _navbar.md 文件，导航栏
		loadNavbar: true,
		subMaxLevel: 2,
		logo: '/_images/logo.png',
		// disqus: 'shortname',
		search: {
			noData: {
				'/': '没有结果!'
			},
			paths: 'auto',
			placeholder: {
				'/': '搜索'
			}
		},
		// docsify-copy-code
		copyCode: {
			buttonText: '点击复制',
			errorText: '错误',
			successText: '复制成功'
		},
		plugins: [
			EditOnGithubPlugin.create('https://gitee.com/yl-yue/yue-library/tree/master/docs/',
				'https://gitee.com/yl-yue/yue-library/tree/master/docs/',
				'去码云编辑此页')
		]
	}
}
}
