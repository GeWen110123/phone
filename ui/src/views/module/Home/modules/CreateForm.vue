<template>
  <a-drawer width="35%" :label-col="4" :wrapper-col="14" :visible="open" @close="onClose">
    <a-divider orientation="left">
      <b>{{ formTitle }}</b>
    </a-divider>
    <a-form-model ref="form" :model="form" :rules="rules">
      <a-form-model-item label="操作用户" prop="user" >
        <a-input v-model="form.user" placeholder="请输入操作用户" />
      </a-form-model-item>
      <a-form-model-item label="操作时间" prop="time" >
        <a-date-picker style="width: 100%" v-model="form.time" format="YYYY-MM-DD HH:mm:ss" allow-clear/>
      </a-form-model-item>
      <a-form-model-item label="查询部门" prop="deptName" >
        <a-input v-model="form.deptName" placeholder="请输入查询部门" />
      </a-form-model-item>
      <a-form-model-item label="同户名" prop="userName" >
        <a-input v-model="form.userName" placeholder="请输入同户名" />
      </a-form-model-item>
      <a-form-model-item label="操作内容" prop="content" >
        <editor ref="contentEditor" v-model="form.content" />
      </a-form-model-item>
      <a-form-model-item label="类型" prop="type" >
      </a-form-model-item>
      <div class="bottom-control">
        <a-space>
          <a-button type="primary" :loading="submitLoading" @click="submitForm">
            保存
          </a-button>
          <a-button type="dashed" @click="cancel">
            取消
          </a-button>
        </a-space>
      </div>
    </a-form-model>
  </a-drawer>
</template>

<script>
import { getInquireall, addInquireall, updateInquireall } from '@/api/module/Home'
import Editor from '@/components/Editor'

export default {
  name: 'CreateForm',
  props: {
  },
  components: {
    Editor
  },
  data () {
    return {
      submitLoading: false,
      formTitle: '',
      contentEditor: '',
      // 表单参数
      form: {
        id: null,

        user: null,

        time: null,

        deptName: null,

        userName: null,

        content: null,

        type: null

      },
      // 1增加,2修改
      formType: 1,
      open: false,
      rules: {
        user: [
          { required: true, message: '操作用户不能为空', trigger: 'blur' }
        ],

        time: [
          { required: true, message: '操作时间不能为空', trigger: 'blur' }
        ],

        deptName: [
          { required: true, message: '查询部门不能为空', trigger: 'blur' }
        ],

        userName: [
          { required: true, message: '同户名不能为空', trigger: 'blur' }
        ]

      }
    }
  },
  filters: {
  },
  created () {
  },
  computed: {
  },
  watch: {
  },
  mounted () {
  },
  methods: {
    onClose () {
      this.open = false
    },
    // 取消按钮
    cancel () {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset () {
      this.formType = 1
      this.form = {
        id: null,

        user: null,

        time: null,

        deptName: null,

        userName: null,

        content: null,

        type: null

      }
    },
    /** 新增按钮操作 */
    handleAdd (row) {
      this.reset()
      this.formType = 1
      this.open = true
      this.formTitle = '添加'
    },
    /** 修改按钮操作 */
    handleUpdate (row, ids) {
      this.reset()
      this.formType = 2
      const id = row ? row.id : ids
      getInquireall(id).then(response => {
        this.form = response.data
        this.open = true
        this.formTitle = '修改'
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.form.content = this.contentEditor.getValue()
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.form.id !== undefined && this.form.id !== null) {
            updateInquireall(this.form).then(response => {
              this.$message.success(
                '修改成功',
                3
              )
              this.open = false
              this.$emit('ok')
            }).finally(() => {
              this.submitLoading = false
            })
          } else {
            addInquireall(this.form).then(response => {
              this.$message.success(
                '新增成功',
                3
              )
              this.open = false
              this.$emit('ok')
            }).finally(() => {
              this.submitLoading = false
            })
          }
        } else {
          return false
        }
      })
    }
  }
}
</script>
