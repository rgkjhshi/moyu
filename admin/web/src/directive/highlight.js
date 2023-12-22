// highlight.js 代码高亮指令(这是自定义的插件)
import Highlight from 'highlight.js' // 导入代码高亮文件
import 'highlight.js/styles/a11y-light.css' // 代码高亮风格，选择更多风格需导入 node_modules/hightlight.js/styles/ 目录下其它css文件

// 自定义插件
const install = function(Vue) {
  // 自定义一个代码高亮指令
  Vue.directive('highlight', function(el) {
    const blocks = el.querySelectorAll('pre code')
    blocks.forEach((block) => {
      Highlight.highlightBlock(block)
    })
  })
}

Highlight.install = install
export default Highlight
