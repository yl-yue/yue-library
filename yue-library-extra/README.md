# yue-library子级模块
此处定义的module为当前版本的子级module：
- 子级module属于当前版本已成熟或暂不需要频繁迭代维护的模块
- 区分顶级module与子级module的目的在于，进行多模块打包时，可以加快构建速度并且增强模块独立维护性（子级module未迭代且兼容的情况下，不再随主版本一起发布）
- 顶级module与子级module会在不同版本进行对调