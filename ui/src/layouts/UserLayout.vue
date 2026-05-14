<template>
  <div id="userLayout" :class="['user-layout-wrapper', isMobile && 'mobile']">
    <div class="login-wrapper">
      <div class="login-bgc">
        <div class="login-title relative"><svg data-v-b5c1c6c0="" class="inline-block text-32px text-primary"
            width="1em" height="1em" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 476.22 476.22">
            <path fill="#0e42d2"
              d="M389.98,400.6c-64.58,76.59-176.66,93.98-261.1,38.54-57.75-37.91-92.29-103.74-87.69-173.54,1.28-19.35,9.54-58.77,33.33-61.44,20.49-2.29,43.2,14.41,62.99,21.14,64.93,22.1,150.88,21.39,214.51-4.81,17.24-7.1,39.23-23.9,58.05-13.31,25.61,14.41,27.13,67.07,24.38,92.75-3.94,36.78-20.9,72.7-44.47,100.65ZM150.91,269c-17.4-4.01-34.19-9.87-50.5-17.04-2.56-1.12-15.17-8.19-16.64-6.83-23.51,92.25,48.81,179.87,140.64,188.01,3.68.33,9,.72,12.64.79,3.76.07,7.18-.41,10.86-.78-.24-3.7-1.05-7.05-.77-10.85,1.3-17.71,28.61-26.36,26.88-43.25-.89-8.76-14.85-11.03-22.02-11.55-8.23-.6-17.94,1.77-25.24-3.11-5.39-3.61-5.08-10.32-7.37-15.76-4.74-11.26-15.86-17.54-27.79-18.47-22.55-1.77-68.52,7.5-81.87-16.63-11.77-21.27,12.24-35.1,29.25-40.42,3.38-1.06,7.52-1.31,10.88-2.55,1.03-.38,1.28.27,1.04-1.56ZM391.83,245.21c-1.03-.58-12.51,5.5-14.65,6.42-20.82,8.92-42.01,16.02-64.2,20.87-10.31,2.25-21.7,3.15-31.66,5.65-19.17,4.82-17.59,22.78-.42,29.11,10.27,3.78,25.34,3.94,36.56,5.97,23.56,4.26,56.74,11.28,53.56,41.93-.31,2.98-2.13,5.86-2.26,8.2-.08,1.48.18,1.21,1.5,1.13,5.18-9.87,11.46-19.09,15.7-29.46,7.55-18.44,12.19-42.75,11.17-62.68-.19-3.67-2.95-25.81-5.31-27.15Z">
            </path>
            <path fill="#0e42d2"
              d="M278.22,21.54c4.79,5.69,9.27,14.39,11.84,21.37.67,1.8,1.49,7.86,2.59,8.61,1.49,1.01,17.78-3.63,21.39-4.05,71.76-8.37,101.88,65.14,44.46,110.3-41.92,32.98-84.43,32.67-135.62,31.41-47.74-1.18-111.25-13.52-129.97-64.78-19.66-53.81,31.01-83.19,77.15-58.5,2,1.07,11.58,7.79,12.54,7.59,1.57-.32,1.05-4.62,1.34-6.14,3.75-19.76,11.37-39.47,27.8-52.04,20.82-15.93,49.31-14.16,66.47,6.23ZM256.71,71.1c1.07-24.31-18.03-42.71-30.8-12.52-5.88,13.9-7.77,32.56-3.36,47,1.73.46,1.24-.41,1.7-.95,10.15-11.8,19.02-25.11,32.46-33.52ZM340.18,123.96c10.12-10.37,17.92-27.48.94-35.85-29-14.31-83.74,17.89-87.91,49.24-1.6,11.97,2.2,14.36,13.71,14.53,21.84.32,57.86-12.13,73.26-27.92ZM173.64,117.92c-4.77-6.36-9.81-11.55-16.53-15.93-7.07-4.62-24.6-13.3-29.47-2.16-3.94,9.02,6.8,23.05,13.43,28.86,12.36,10.83,29.63,16.76,45.66,19.42-.28-11.02-6.62-21.58-13.08-30.19Z">
            </path>
          </svg>
          <h3 class="ml-10px text-20px font-400">手机监测平台</h3>
        </div>
        <div class="w-40"><img class="size-full" src="../assets/svg-icon/login-background.svg"></div>

        <div class=" w-full ">
          <h1 class="text-24px font-400">现代化的手机监测平台</h1>
          <p class="mt-8px text-14px color-gray-500">提供全方位的手机监控与管理解决方案</p>
        </div>

        <div class="wave-bg">
          <div class="geometric-decorations">
            <div class="geo-element circle-outline animate-fade-in-up animate-delay-0s"></div>
            <div class="geo-element square-rotated animate-fade-in-left animate-delay-0s"></div>
            <div class="geo-element circle-small animate-fade-in-up animate-delay-0.3s"></div>
            <div class="geo-element square-bottom-right animate-fade-in-right animate-delay-0s"></div>
            <div class="geo-element bg-bubble animate-scale-in animate-delay-0.5s"></div>
            <div class="geo-element circle-top-right animate-fade-in-down animate-delay-0.5s"></div>
            <div class="geo-element dot dot-top-left animate-bounce-in animate-delay-0s"></div>
            <div class="geo-element dot dot-top-right animate-bounce-in animate-delay-0s"></div>
            <div class="geo-element dot dot-center-right animate-bounce-in animate-delay-0s"></div>
            <div class="squares-group"><i
                class="geo-element square square-blue animate-fade-in-left-rotated-blue animate-delay-0.2s"></i><i
                class="geo-element square square-pink animate-fade-in-left-rotated-pink animate-delay-0.4s"></i><i
                class="geo-element square square-purple animate-fade-in-left-no-rotation animate-delay-0.6s"></i></div>
          </div>
        </div>
      </div>

      <div class="login-content">
        <div class="login-box">
          <div class="mb-5px">登录到您的账户</div>
          <div class="pb-18px">欢迎回来！请输入您的账户信息</div>
          <router-view />

        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { deviceMixin } from '@/store/device-mixin'

export default {
  name: 'UserLayout',
  mixins: [deviceMixin],
  // mounted() {
  //   document.body.classList.add('userLayout')
  //   document.body.style.overflow = 'hidden';
  //   document.html.style.overflow = 'hidden';
  //   document.documentElement.style.overflow = 'hidden';

  // },
  // beforeDestroy() {
  //   document.body.classList.remove('userLayout')
  //   document.body.style.overflow = '';
  //   document.html.style.overflow = '';
  //   document.documentElement.style.overflow = '';
  // },
}
</script>

<style lang="less" scoped>
#userLayout.user-layout-wrapper {
  height: 100%;

  &.mobile {
    .container {
      .main {
        max-width: 368px;
        width: 98%;
      }
    }
  }

  .userBox {
    color: #000;
    width: 100%;
    min-height: 100%;
    // background: #f0f2f5 url(~@/assets/background.png) no-repeat 50%;
    background-size: 100%;
    position: relative;

    display: flex;
    align-items: center;
    justify-content: right;
    padding-right: 8%;
  }

  .container {
    padding: 44px 45px;
    background-color: #fff;
    border-radius: 4%;

    a {
      text-decoration: none;
    }

    .top {
      .header {
        height: 44px;
        line-height: 34px;

        .badge {
          position: absolute;
          display: inline-block;
          line-height: 1;
          vertical-align: middle;
          margin-left: -12px;
          margin-top: -10px;
          opacity: 0.8;
        }

        .logo {
          height: 44px;
          vertical-align: top;
          margin-right: 16px;
          border-style: none;
        }

        .sz {
          padding-bottom: 6px;
          border-bottom: 2px solid #028bff;
        }

        .title {
          font-size: 23px;
          color: rgba(0, 0, 0, 0.85);
          font-family: Avenir, 'Helvetica Neue', Arial, Helvetica, sans-serif;
          font-weight: 600;
          position: relative;
          top: 2px;
        }
      }

      .desc {
        font-size: 14px;
        color: rgba(0, 0, 0, 0.45);
        margin-top: 12px;
        margin-bottom: 40px;
      }
    }

    .main {
      min-width: 260px;
      width: 368px;
      margin: 0 auto;
    }

    .footer {
      position: absolute;
      width: 100%;
      bottom: 0;
      padding: 0 16px;
      margin: 48px 0 24px;
      text-align: center;

      .links {
        margin-bottom: 8px;
        font-size: 14px;

        a {
          color: rgba(0, 0, 0, 0.45);
          transition: all 0.3s;

          &:not(:last-child) {
            margin-right: 40px;
          }
        }
      }

      .copyright {
        color: rgba(0, 0, 0, 0.45);
        font-size: 14px;
      }
    }
  }
}
</style>

<style lang="less">
:root {
  --primary-100-color: 181, 211, 255;
  /* 红色 */
  --primary-50-color: 230, 241, 255;
  /* 蓝色 */
  --primary-color: 14, 66, 210;
}

.login-bgc {
  width: 65%;
  background-color: #E6F1FF;
  position: relative;
  overflow: hidden;

  .relative {
    position: relative;
  }

  .w-40 {
    position: absolute;
    width: 40%;
    margin: auto;
    z-index: 10;
    bottom: 10.5%;
    top: 20%;
    left: 0;
    right: 0;
  }

  .w-full {
    position: absolute;
    text-align: center;
    width: 100%;
    bottom: 80px;
  }

  .size-full {
    width: 100%;
    height: 100%;
    max-width: 100%;
    height: auto;
    display: block;
    vertical-align: middle;
  }

  .login-title {
    display: flex;
    align-items: center;
    tab-size: 4;
    z-index: 100;
    padding-left: 30px;
    padding-top: 30px;
    font-size: 20px;
    color: #000;

    .text-32px {
      font-size: 32px;
    }

    h3 {
      margin: 0 8px;
    }
  }
}

.wave-bg {
  .geometric-decorations {
    .circle-outline {
      position: absolute;
      top: 10%;
      left: 25%;
      width: 42px;
      height: 42px;
      border: 2px solid #B5D3FF;
      border-radius: 50%;
    }

    .square-rotated {
      position: absolute;

      top: 50%;
      left: 16%;
      width: 60px;
      height: 60px;
      transform: rotate(45deg);
      /* 旋转45度 */
      transform-origin: center;
      /* 设置旋转的中心为元素的中心 */
      background-color:
        color-mix(in srgb, rgb(var(--primary-100-color)) 80%, rgb(var(--primary-50-color)));
    }

    .bg-bubble {
      position: absolute;

      top: -120px;
      right: -120px;
      width: 360px;
      height: 360px;
      background-color:
        color-mix(in srgb, rgb(var(--primary-100-color)) 80%, rgb(var(--primary-50-color)));
      border-radius: 50%;
    }

    .circle-top-right {
      position: absolute;
      top: 3%;
      right: 3%;
      z-index: 100;
      width: 50px;
      height: 50px;
      cursor: pointer;
      background:
        color-mix(in srgb, rgb(var(--primary-50-color)) 80%, rgb(var(--primary-50-color)));
      border-radius: 50%;
      transition: all .3s;
    }

    .square-bottom-right {
      position: absolute;

      right: 10%;
      bottom: 10%;
      width: 50px;
      height: 50px;
      background-color: rgb(var(--primary-100-color));
      transform: rotate(60deg);
    }

    .dot.dot-top-right {
      position: absolute;
      width: 14px;
      height: 14px;
      background-color: rgb(var(--primary-50-color));
      border-radius: 50%;
      top: 140px;
      transform: rotate(-15deg);
      right: 120px;
    }

    .squares-group {
      position: absolute;
      bottom: 18px;
      left: 20px;
      width: 140px;
      height: 140px;
      pointer-events: none;


      .square {
        position: absolute;
        display: block;
        border-radius: 8px;
        box-shadow: 0 8px 24px #4057a71f;

      }

      .square-blue {
        top: 12px;
        left: 30px;
        z-index: 2;
        width: 50px;
        height: 50px;
        transform: rotate(-15deg);
        background-color: rgb(from rgb(var(--primary-color)) r g b / 30%);
      }

      .square.square-pink {
        transform: rotate(10deg);
        top: 30px;
        left: 48px;
        z-index: 1;
        width: 70px;
        height: 70px;
        background-color: rgb(from rgb(var(--primary-color)) r g b / 15%);
      }

      .square.square-purple {
        top: 66px;
        left: 86px;
        z-index: 3;
        width: 32px;
        height: 32px;
        background-color: rgb(from rgb(var(--primary-color)) r g b / 45%);
      }
    }
  }

}

// #body,html {
//   width: 100%;
//   height: 100%;
//   overflow: hidden;
// }

.login-wrapper {
  width: 100%;
  height: 100%;
  display: flex;





  .login-content {
    width: calc(100% - 65%);
    height: 100%;
    margin: auto;
    padding-top: 250px;

    .login-box {
      max-width: 450px;
      padding-left: 24px;
      padding-right: 24px;
      height: 630px;
      border-radius: 5px;
      background-size: cover;
      margin: auto;

      .mb-5px {
        font-weight: 600;
        color: #000;
        margin-bottom: 5px;
        font-size: 32px;
      }

      .pb-18px {
        font-size: 16px;
        padding-bottom: 18px;
        color: #a7a7a7;
      }
    }

    .login-Btn {
      width: 270px;
      height: 64px;
      margin-top: 25px;
      cursor: pointer;
      background: url('../assets/images/loginBtn.png') no-repeat;

      &:hover {
        background: url('../assets/images/loginBtnHover.png') no-repeat;
      }
    }
  }
}
</style>
