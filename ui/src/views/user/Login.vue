<template>
  <div class="main">
    <a-form-model id="formLogin" ref="form" class="user-layout-login" :model="form" :rules="rules">

      <a-alert v-if="isLoginError" type="error" showIcon style="margin-bottom: 10px" :message="loginErrorInfo" closable
        :after-close="handleCloseLoginError" />
      <a-form-model-item prop="username" style="margin-top: 26px;">
        <a-input v-model.trim="form.username" class="login-input user-wrapper" size="large" placeholder="账户">
          <a-icon slot="prefix" type="user" :style="{ color: 'rgba(0,0,0,.25)' }" />
        </a-input>
      </a-form-model-item>
      <a-form-model-item prop="password" style="margin-top: 26px;">
        <a-input-password v-model.trim="form.password" class="login-input pwd-wrapper" size="large" placeholder="密码">
          <a-icon slot="prefix" type="lock" :style="{ color: 'rgba(0,0,0,.25)' }" />
        </a-input-password>
      </a-form-model-item>
   

      <!-- <a-row :gutter="16" v-if="captchaEnabled">
        <a-col class="gutter-row" :span="16">
          <a-form-model-item prop="code">
            <a-input v-model.trim="form.code" size="large" type="text" autocomplete="off" placeholder="验证码">
              <a-icon slot="prefix" type="security-scan" :style="{ color: 'rgba(0,0,0,.25)' }" />
            </a-input>
          </a-form-model-item>
        </a-col>
        <a-col class="gutter-row" :span="8">
          <img class="getCaptcha" :src="codeUrl" @click="getCode" />
        </a-col>
      </a-row> -->
      <a-form-model-item prop="rememberMe" style="margin-top: 26px;">
        <a-checkbox style="color:#838383" :checked="form.rememberMe" @change="rememberMe">记住密码</a-checkbox>

      </a-form-model-item>
      <a-form-item style="margin-top: 4px;margin-top: 26px;" >
        <a-button size="large" type="primary" htmlType="submit" class="login-button" :loading="logining"
          :disabled="logining" @click="handleSubmit">登录</a-button>
      </a-form-item>
    </a-form-model>

    <!-- 修改密码抽屉 -->
    <update-password ref="updatePassword" />
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import { timeFix } from '@/utils/util'
import { getCodeImg , ipApplication } from '@/api/login'
import UpdatePassword from '@/views/account/settings/UpdatePassword'

import { LOGIN_USERNAME, LOGIN_PASSWORD, LOGIN_REMEMBERME } from '@/store/mutation-types'
import storage from 'store'

export default {
  components: {
    UpdatePassword,
  },
  data() {
    return {
      codeUrl: '',
      isLoginError: false,
      loginErrorInfo: '',
      form: {
        username: '',
        password: '',
        code: undefined,
        uuid: '',
        tcode: null,

        rememberMe: false,
      },
      downLoadUrl: '',
      rules: {
        username: [{ required: true, message: '请输入帐户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        // code: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
      },
      logining: false,
      captchaEnabled: true,
    }
  },
  created() {
    this.downLoadUrl = window.origin + '/download/firefox.exe'
    this.getStorage()
    // this.getCode()
  },
  mounted() { },
  methods: {
    getCode() {
      getCodeImg().then((res) => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
        if (this.captchaEnabled) {
          this.codeUrl = 'data:image/gif;base64,' + res.img
          this.form.uuid = res.uuid
        }
      })
    },
    getStorage() {
      const username = storage.get(LOGIN_USERNAME)
      const password = storage.get(LOGIN_PASSWORD)
      const rememberMe = storage.get(LOGIN_REMEMBERME)
      if (username) {
        this.form = {
          username: username,
          password: password,
          rememberMe: rememberMe,
        }
      }
    },
    rememberMe(e) {
      this.form.rememberMe = e.target.checked
    },
    ...mapActions(['Login', 'Logout']),
    handleSubmit() {
      this.logining = true
      this.$refs.form.validate((valid) => {
        if (valid) {
          if (this.form.rememberMe) {
            storage.set(LOGIN_USERNAME, this.form.username)
            storage.set(LOGIN_PASSWORD, this.form.password)
            storage.set(LOGIN_REMEMBERME, this.form.rememberMe)
          } else {
            storage.remove(LOGIN_USERNAME)
            storage.remove(LOGIN_PASSWORD)
            storage.remove(LOGIN_REMEMBERME)
          }
          // this.form.tcode = this.form.tcode ? this.form.tcode : '';

          this.Login(this.form)
            .then((res) => this.loginSuccess(res))
            .catch((err) => this.requestFailed(err))
            .finally(() => {
              this.logining = false
            })
        } else {
          setTimeout(() => {
            this.logining = false
          }, 600)
        }
      })
    },
    loginSuccess(res) {
      if (res) {
        this.$refs.updatePassword.handleUpdatePwd(res.token)
      } else {
        this.$router.push({ path: '/' })
        this.handleCloseLoginError()

      }
    },
    requestFailed(err) {
      let that = this;
      if (err == '用户登录需要申请') {
        this.$confirm({
          title: '系统提示',
          content: '当前IP地址不在白名单，是否申请当前地址为IP白名单地址？',
          onOk() {
            ipApplication(that.form).then((res) => {
              that.$message.success('已申请，请等待管理员审批')
            })
          },
          onCancel() { },
        })
      } else {
        this.isLoginError = true
        this.loginErrorInfo = err
        this.form.code = undefined
        if (this.captchaEnabled) {
          this.getCode()
        }
      }





    },
    handleCloseLoginError() {
      this.isLoginError = false
      this.loginErrorInfo = ''
    },


  },
}
</script>

<style lang="less" scoped>
.main {
  .ant-form-item {
    margin-bottom: 9px;
  }
}

.form_title_size {
  text-align: center;
  font-size: 28px;
  color: #333639;
  margin-bottom: 14px;
}

.user-layout-login {
  label {
    font-size: 14px;
  }

  .getCaptcha {
    display: block;
    width: 100%;
    height: 40px;
  }

  button.login-button {
    padding: 0 15px;
    font-size: 16px;
    height: 40px;
    width: 100%;

    background-color: #0E42D2;
  }

  .user-login-other {
    text-align: left;
    margin-top: 24px;
    line-height: 22px;

    .register {
      float: right;
    }
  }
}
</style>
