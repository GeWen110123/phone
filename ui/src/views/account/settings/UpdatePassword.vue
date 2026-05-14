<template>
  <a-modal :title="title" :visible="open" :confirm-loading="submitLoading" @ok="submitForm" @cancel="cancel">
    <a-form-model ref="form" :model="form" :rules="rules">
      <a-form-model-item has-feedback label="旧密码" prop="oldPassword">
        <a-input-password v-model.trim="form.oldPassword" placeholder="请输入旧密码"  />
      </a-form-model-item>
      <a-form-model-item has-feedback label="新密码" prop="newPassword">
        <a-input-password v-model.trim="form.newPassword" placeholder="请输入新密码"  />
      </a-form-model-item>
      <a-form-model-item has-feedback label="确认密码" prop="confirmPassword">
        <a-input-password v-model.trim="form.confirmPassword" placeholder="请确认密码"  />
      </a-form-model-item>
    </a-form-model>
  </a-modal>
</template>
<script>
import { updateUserPwd } from '@/api/system/user'

export default {
  props: {},
  data() {
    const validateNewPass = (rule, value, callback) => {
      const strongRegex = new RegExp('^(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$', 'g');
      const maxLength = 20;

      if (value === '') {
        callback(new Error('请输入新密码'));
      } else if (!strongRegex.test(value)) {
        callback(new Error('密码必须包含大小写字母、数字和特殊字符，且长度至少为8个字符'));
      } else if (value.length > maxLength) {
        callback(new Error(`密码不能超过 20 个字符，当前长度为 ${value.length}`));
      } else {
        if (this.form.confirmPassword !== '') {
          this.$refs.form.validateField('confirmPassword');
        }
        callback();
      }
    }
    const validateConfirmPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入新密码确认'))
      } else if (value !== this.form.newPassword) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      submitLoading: false,
      title: '重置密码',
      open: false,
      childrenDrawer: false,
      formLayout: 'horizontal',
      form: {
        oldPassword: undefined,
        newPassword: undefined,
        confirmPassword: undefined,
      },
      token: '',
      rules: {
        oldPassword: [{ required: true, message: '密码不能为空', trigger: 'blur' }],
        newPassword: [{ required: true, validator: validateNewPass, trigger: 'change' }],
        confirmPassword: [{ required: true, validator: validateConfirmPass, trigger: 'change' }],
      },
      layout: {
        labelCol: { span: 4 },
        wrapperCol: { span: 14 },
      },
    }
  },
  methods: {
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        oldPassword: undefined,
        newPassword: undefined,
        confirmPassword: undefined,
      }
    },
    handleUpdatePwd(token) {
      this.token = token ? token : ''
      this.open = true
    },
    /** 重置密码按钮操作 */
    submitForm: function () {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.submitLoading = true

          updateUserPwd(this.form.oldPassword, this.form.newPassword, this.token ? this.token : '')
            .then((response) => {
              this.$message.success('修改成功', 3)
              this.open = false
            })
            .finally(() => {
              this.submitLoading = false
            })

            .catch((error) => {
              this.$message.error(error, 3)

            })
        } else {
          return false
        }
      })
    },
  },
}
</script>
