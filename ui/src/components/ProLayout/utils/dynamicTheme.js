import client from 'webpack-theme-color-replacer/client'
import {
  generate
} from '@ant-design/colors'
import {
  message
} from 'ant-design-vue'

export const themeColor = {
  /**
   * 获取Ant Design系列颜色
   *
   * @param color 基础颜色，格式为十六进制颜色代码，例如 "#1DA57A"
   * @returns 包含基础颜色、渐变色和RGB值的数组
   */
  getAntdSerials(color) {
    // 淡化（即less的tint）
    const lightens = new Array(9).fill().map((t, i) => {
      return client.varyColor.lighten(color, i / 10)
    })
    // colorPalette 变换得到颜色值
    const colorPalettes = generate(color)
    const rgb = client.varyColor.toNum3(color.replace('#', '')).join(',')
    return lightens.concat(colorPalettes).concat(rgb)
  },
  /**
   * 改变颜色
   *
   * @param {string} newColor 新的颜色值
   * @returns {Promise} 返回一个Promise对象，表示颜色改变的结果
   */
  changeColor(newColor) {
    const options = {
      newColors: this.getAntdSerials(newColor), // new colors array, one-to-one corresponde with `matchColors`
      changeUrl(cssUrl) {
        return `/${cssUrl}` // while router is not `hash` mode, it needs absolute path
      }
    }
    return client.changer.changeColor(options, Promise)
  }
}

/** 
 * 更新主题颜色的函数
 *
 * @param {string} newPrimaryColor 新的主色值
 * @returns {void} 无返回值
 */
export const updateTheme = newPrimaryColor => {
  const hideMessage = message.loading('', 0)
  themeColor.changeColor(newPrimaryColor).then(r => {
    hideMessage()
  })
}

/**
 * 更新页面颜色弱视模式
 *
 * @param {boolean} colorWeak 是否开启颜色弱视模式
 */
export const updateColorWeak = colorWeak => {
  // document.body.className = colorWeak ? 'colorWeak' : '';
  const app = document.body.querySelector('#app')
  colorWeak ? app.classList.add('colorWeak') : app.classList.remove('colorWeak')
}