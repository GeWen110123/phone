<template>
  <div>
    <a-list itemLayout="horizontal" class="header-list" :dataSource="data">
      <a-list-item slot="renderItem" slot-scope="item, index" :key="index">
        <a-list-item-meta>
          <a slot="title">{{ item.title }}</a>
          <span slot="description">
            <span class="security-list-description">{{ item.description }}</span>
          </span>
        </a-list-item-meta>
        <template v-if="item.actions">
          <a slot="actions" @click="item.actions.callback">{{ item.actions.title }}</a>
        </template>
      </a-list-item>
    </a-list>
    <!-- 修改密码抽屉 -->
    <update-password ref="updatePassword" />


  </div>
</template>

<script>
import UpdatePassword from './UpdatePassword'
import { mapGetters } from 'vuex'

export default {
  name: 'SecuritySettings',
  components: {
    UpdatePassword
  },
  data() {
    const validateIp = (rule, value, callback) => {
      // 判断空值
      if (!value || value == '') {
        callback(new Error('IP 白名单地址不能为空'));
        return;
      }
      // 按逗号分隔多个 IP 地址
      const ips = value.split(',');

      // 限制最多只能输入三个 IP 地址
      if (ips.length > 3) {
        callback(new Error('最多只能输入三个 IP 地址'));
        return;
      }

      // 遍历每一个 IP 地址进行验证
      for (let ip of ips) {
        const parts = ip.trim().split('.'); // 拆分成四部分
        if (parts.length !== 4) {
          callback(new Error(`IP 地址格式错误: ${ip}，应该包含四个部分`));
          return;
        }

        // 判断每一部分是否是合法的数字，并且在 0 到 255 之间
        for (let part of parts) {
          const num = parseInt(part, 10);  // 转换为数字
          if (isNaN(num) || num < 0 || num > 255) {
            callback(new Error(`IP 地址格式错误: ${ip}，每一部分的值应该在 0 到 255 之间`));
            return;
          }
        }
      }
      // 如果没有发现错误，调用 callback() 表示验证通过
      callback();
    };
    return {
      open: false,
      form: {
        loginIpUser: undefined,
      },
      submitLoading: false,
      formLayout: 'horizontal',
      rules: {
        loginIpUser: [{ required: true, message: 'IP白名单地址不能为空', trigger: 'blur' }, { validator: validateIp, trigger: 'blur' }],
      },
      data: [
        { title: '账户密码', description: '当前密码强度', value: '强', actions: { title: '修改', callback: () => { this.$refs.updatePassword.handleUpdatePwd() } } },
        // { title: 'IP白名单', description: '当前IP白名单地址： ', value: '', actions: { title: '修改', callback: () => { this.open = true } } }
      ]
    }
  },
  computed: {
    ...mapGetters(['user']),
    ipWhitelist() {
      // 根据 user.loginIpNew 动态更新 'IP白名单' 项的 value
      const ipWhitelist = this.data.find(item => item.title === 'IP白名单');
      if (ipWhitelist) {
        ipWhitelist.value = this.user.loginIpNew;
      }
      return ipWhitelist; // 返回更新后的数据
    }
  },

  methods: {
    parseIpList(ipString) {
      if (!ipString) return
      return ipString.split(',');
    },

    cancel() {
      this.open = false
      this.form = {
        oldPassword: undefined,
        newPassword: undefined,
        confirmPassword: undefined,
      }
    },
    /** 重置密码按钮操作 */
    submitForm: function () {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.submitLoading = true
          let data = {
            loginIpUser: this.form.loginIpUser
          }
          WhitelistIp(data)
            .then((response) => {
              this.$message.success('已提交申请', 3)
              this.open = false
            })
            .finally(() => {
              this.submitLoading = false
            })
        } else {
          return false
        }
      })
    },
  }
}
</script>

<style lang="less" scoped>
/deep/.header-list {
  .ant-list-items {
    display: block !important;
  }
}
</style>
