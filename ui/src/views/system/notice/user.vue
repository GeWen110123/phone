<template>
  <page-header-wrapper :breadcrumb="false">
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item label="公告标题">
                <a-input v-model="queryParam.noticeTitle" placeholder="请输入" allow-clear />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item label="公告类型">
                <a-select placeholder="请选择" v-model="queryParam.type" style="width: 100%" allow-clear>
                  <a-select-option v-for="(d, index) in dict.type['sys_notice_type']" :key="index" :value="d.value">{{
                    d.label
                  }}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-button type="primary" @click="handleQuery"><a-icon type="search" />查询</a-button>
              <a-button style="margin-left: 8px" @click="resetQuery"><a-icon type="redo" />重置</a-button>
            </a-col>
          </a-row>
        </a-form>
      </div>

      <!-- 数据展示 -->
      <a-table
        :loading="loading"
        :size="tableSize"
        rowKey="noticeId"
        :columns="columns"
        :data-source="list"
        :pagination="false"
        :bordered="tableBordered"
      >
        <span slot="noticeType" slot-scope="text, record">
          <dict-tag :options="dict.type['sys_notice_type']" :value="record.message.noticeType" />
        </span>
        <span slot="status" slot-scope="text, record">
          <dict-tag :options="dict.type['sys_normal_disable']" :value="record.message.status" />
        </span>
        <span slot="createTime" slot-scope="text, record">
          {{ parseTime(record.createTime) }}
        </span>
        <span slot="operation" slot-scope="text, record">
          <a @click="$refs.noticeDetail.getNotice(record.message.noticeId, record.id)"> <a-icon type="edit" />查看 </a>
        </span>
      </a-table>
      <!-- 分页 -->
      <a-pagination
        class="ant-table-pagination"
        show-size-changer
        show-quick-jumper
        :current="queryParam.pageNum"
        :total="total"
        :page-size="queryParam.pageSize"
        :showTotal="(total) => `共 ${total} 条`"
        @showSizeChange="onShowSizeChange"
        @change="changeSize"
      />
    </a-card>

    <notice-detail ref="noticeDetail" :typeOptions="typeOptions" />
  </page-header-wrapper>
</template>

<script>
import { listNotice, delNotice } from '@/api/system/notice'
import { tableMixin } from '@/store/table-mixin'
import NoticeDetail from '@/components/NoticeIcon/NoticeDetail.vue'

export default {
  name: 'Notice',
  components: { NoticeDetail },
  mixins: [tableMixin],
  dicts: ['sys_notice_status', 'sys_notice_type', 'sys_normal_disable'],
  data() {
    return {
      list: [],
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
      queryParam: {
        pageNum: 1,
        pageSize: 10,
        noticeTitle: undefined,
        status: undefined,
        type: '',
      },
      typeOptions: [],
      columns: [
        {
          title: '公告标题',
          dataIndex: 'message.noticeTitle',
          align: 'center',
        },
        {
          title: '公告类型',
          dataIndex: 'noticeType',
          scopedSlots: { customRender: 'noticeType' },
          align: 'center',
        },

        {
          title: '发布时间',
          dataIndex: 'createTime',
          scopedSlots: { customRender: 'createTime' },
          align: 'center',
        },
        // {
        //   title: '发布人员',
        //   dataIndex: 'message.createBy',
        //   ellipsis: true,
        //   align: 'center',
        // },
        {
          title: '操作',
          dataIndex: 'operation',
          width: '15%',
          scopedSlots: { customRender: 'operation' },
          align: 'center',
        },
      ],
    }
  },
  filters: {},
  created() {
    this.fetchNotice()
  },
  computed: {},
  watch: {
    $route: {
      immediate: true,
      handler(to) {
        let that = this
        if (to.name == 'NoticeUser') {
          if (to.query?.noticeId) {
            setTimeout(() => {
              that.$refs.noticeDetail.getNotice(to.query.noticeId, to.query.id)
            }, 500)
          }
        }
      },
    },
  },
  methods: {
    /** 查询公告列表 */
    getList() {
      this.loading = true
      listNotice(this.queryParam).then((response) => {
        response.rows.forEach((item) => {
          item.message = JSON.parse(item.message)
        })

        this.list = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParam.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParam = {
        pageNum: 1,
        pageSize: 10,
        noticeTitle: undefined,
        status: undefined,
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

    fetchNotice() {
      this.resetQuery()
      if (this.typeOptions.length === 0) {
        this.getDicts('sys_notice_type').then((response) => {
          this.typeOptions = response.data
        })
      }
      this.getList()
    },
  },
}
</script>
