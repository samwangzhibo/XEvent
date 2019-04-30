### 目录讲解 
---

- build 
  - `libxevent.js` (产出文件)
  - `libxevent.js.map`
- node_modules (node依赖的类库)
- src (xevent源码)
- `webpack.config.js`  (webpack打包的配置)
- `package-lock.json`
- `package.json`


#### webpack安装
---

[webpack安装(mac教程)](https://blog.csdn.net/github_40020301/article/details/80857223)


#### 构建
---

1. 修改 `XPRegisterTrackers.js` 里面的 `isTest = 0`
2. npm start
```shell
npm start
```



#### 产物
---

`build` 里面 `libxevent.js`



#### 调试模式
---

1. 修改 `XPRegisterTrackers.js` 里面的 `isTest = 1`
2. 运行 `test.js` 文件

