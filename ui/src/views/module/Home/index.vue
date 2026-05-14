<template>
  <a-card :bordered="false">
    <a-empty image="https://gw.alipayobjects.com/mdn/miniapp_social/afts/img/A*pevERLJC9v0AAAAAAAAAAABjAQAAAQ/original"
      :image-style="{
        height: '280px',
      }">
      <span slot="description"> 暂未开发 </span>
    </a-empty>
  </a-card>
</template>

<script>
import { delInquireall } from '@/api/module/Home'
import CreateForm from './modules/CreateForm'
import { tableMixin } from '@/store/table-mixin'
import { mapGetters } from 'vuex'

export default {
  name: 'Home',
  components: {
    CreateForm
  },
  mixins: [tableMixin],
  data() {
    return {
      list: [],
      selectedRowKeys: [],
      selectedRows: [],
      // 高级搜索 展开/关闭
      advanced: false,
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      ids: [],
      loading: false,
      total: 0,
      // 查询参数
      queryParam: {
        pageNum: 1,
        pageSize: 10
      },
      columns: [
  
      ]
    }
  },
  filters: {
  },
  created() {
    this.getList()
  },


  methods: {
    /** 查询搜索日志列表 */
    getList() {
      this.loading = true
      // listInquireall(this.queryParam).then(response => {
      //   this.list = response.rows
      //   this.total = response.total
      //   this.loading = false
      // })
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParam.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParam = {
        user: undefined,
        time: undefined,
        deptName: undefined,
        userName: undefined,
        content: undefined,
        type: undefined,
        pageNum: 1,
        pageSize: 10
      }
      this.handleQuery()
    },
    onShowSizeChange(current, pageSize) {
      this.queryParam.pageSize = pageSize
      this.getList()
    },
    changeSize(current, pageSize) {
      this.queryParam.pageNum = current
      this.queryParam.pageSize = pageSize
      this.getList()
    },
    onSelectChange(selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
      this.ids = this.selectedRows.map(item => item.id)
      this.single = selectedRowKeys.length !== 1
      this.multiple = !selectedRowKeys.length
    },
    toggleAdvanced() {
      this.advanced = !this.advanced
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      var that = this
      const ids = row.id || this.ids
      this.$confirm({
        title: '确认删除所选中数据?',
        content: '当前选中编号为' + ids + '的数据',
        onOk() {
          return delInquireall(ids)
            .then(() => {
              that.onSelectChange([], [])
              that.getList()
              that.$message.success(
                '删除成功',
                3
              )
            })
        },
        onCancel() { }
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      var that = this
      this.$confirm({
        title: '是否确认导出?',
        content: '此操作将导出当前条件下所有数据而非选中数据',
        onOk() {
          that.download('module/inquireall/export', {
            ...that.queryParam
          }, `inquireall_${new Date().getTime()}.xlsx`)
        },
        onCancel() { }
      })
    }
  }
}
</script>
