<!-- <template>
  <page-header-wrapper :breadcrumb="false">

    <a-layout-content style="padding: 50px;">
      <a-row type="flex" justify="center">
        <a-col :span="12">
          <div class="title-box" :style="{ 'margin-top': tableShow ? '0' : '22%' }">
            <transition name="slide-fade">
              <img v-show="!tableShow" class="search-icon" src="@/assets/images/search.png" />
            </transition>

            <a-input-search v-model="queryParam.accountName" placeholder="只能搜索一个抖音号，内容为精准匹配" :loading="searchLoading"
              enter-button="搜索" size="large" @search="onSearch" />
          </div>
        </a-col>
      </a-row>
      <transition name="bounce">
        <div v-show="tableShow" style="margin-top:30px;">
          <a-table :columns="columns" bordered :data-source="tableData" key="_id" :pagination="false"
            :loading="searchLoading">
            <span slot="createTime" slot-scope="text, record">
              {{ record.timestamp | moment('YYYY-MM-DD HH:mm') }}
            </span>
            <span slot="status" slot-scope="text, record">
              <template v-if="record.check == 0">
                <a-badge color="#ccc">
                </a-badge>
                <span style="color:#B2ADAD;">未核查</span>
              </template>
              <template v-else-if="record.check == 1">
                <a-badge color="#F56C6C">
                </a-badge>
                <span style="color: #F56C6C;">人工核否</span>
              </template>
              <template v-else-if="record.check == 2">
                <a-badge color="#67C23A">
                </a-badge>
                <span style="color: #67C23A;">人工核死</span> 
              </template>
            </span>


            <span slot="content" slot-scope="text, record">
              <div v-html="record.content" :title="record.content">
              </div>
            </span>
            <span slot="operation" slot-scope="text, record">
              <a-divider type="vertical" />
              <a @click="openEditDialog(record)" href="javascript:;"> <a-icon type="edit" />核查</a>
              <a-divider type="vertical" v-hasPermi="['module:management:download']" />
              <a @click="handleExport(record)" v-hasPermi="['module:management:download']">
                <a-icon type="download" />下载
              </a>
            </span>
          </a-table>
          <a-pagination class="ant-table-pagination" :show-total="(total, range) => `共 ${total} 项数据`" show-size-changer
            :default-current="1" :current="queryParam.pageNum" :page-size-options="['10', '30', '50', '100']"
            :total="total" :page-size="queryParam.pageSize" @showSizeChange="onShowSizeChange" @change="changeSize" />
        </div>
      </transition>
    </a-layout-content>

    <a-modal title="查看数据" width='70%' :closable="false" :visible="visible" :confirm-loading="confirmLoading"
      @ok="handleOk" @cancel="handleCancel">
      <div style="min-height: 500px; max-height: 500px; white-space: pre-wrap; overflow: auto; word-wrap: break-word;"
        v-html="detailsData.content">
      </div>
      <template slot="footer">
        <a-popconfirm v-if="detailsData.view_type == 1 && detailsData.exaStatus == 3" placement="bottom" ok-text="确认申请"
          cancel-text="取消" @confirm="confirm">
          <template slot="title">
            <p>即将向管理员申请查询此文档权限</p>
          </template>
          <a-button key="submit" type="primary" @click="handleOk">
            申请查看
          </a-button>
        </a-popconfirm>
        <a-button v-else-if="detailsData.exaStatus == 0" type="info" @click="handleCancel">正在审核中....</a-button>
        <a-button v-else-if="detailsData.exaStatus == 2" type="danger" @click="handleCancel">拒绝查看</a-button>
        <a-button key="back" type="info" @click="handleCancel">
          取消
        </a-button>
      </template>
    </a-modal>


    <a-modal centered width='30%' :visible="editVisible" :closable="false" :confirm-loading="confirmLoading"
      @ok="handleOk" @cancel="handleCancel">
      <p> <a-icon type="exclamation-circle" style="color: #e7a013;    font-size: 15px;
    margin-right: 6px;" />{{ detailsData.msg }}</p>
      <template slot="footer">
        <a-button v-if="detailsData.check != 2" type="success" style="background-color: rgb(72 217 54);
    color: #fff;" @click="changeEdit(2)">
          已核死
        </a-button>
        <a-button v-if="detailsData.check != 1" type="danger" @click="changeEdit(1)">
          已核否
        </a-button>
        <a-button v-if="detailsData.check != 0" style="background-color: #f3ab19;
    color: #fff;" @click="changeEdit(0)">
          重置为未核查
        </a-button>
        <a-button type="info" @click="handleCancel()">
          取消
        </a-button>
      </template>
    </a-modal>
  </page-header-wrapper>


</template>
<script>
import { mapGetters } from 'vuex'

import { management, putVerify, details, verifyLast,getCrawl  } from '@/api/module/UserSearch'
export default {
  name: 'UserSearch',
  components: {
  },
  data() {
    return {
      tableShow: false,
      searchText: '',
      searchType: 1,
      visible: false,
      confirmLoading: false,
      searchLoading: false,
      editVisible: false,
      queryParam: {
        pageNum: 1,
        pageSize: 10,
        accountName: '',
        devId:'12322',
      },
      total: 0,
      tableData: [
      ],
      columns: [
        {
          dataIndex: 'filename',
          key: 'filename',
          title: '文档名称',
          width: 200,
          sorter: true,
        },

        {
          title: '内容',
          dataIndex: 'content',
          scopedSlots: { customRender: 'content' },
          key: 'content',
          ellipsis: true,
          width: 740,

        },

        {
          title: '创建时间',
          key: 'createTime',
          dataIndex: 'createTime',
          scopedSlots: { customRender: 'createTime' },
          width: 200,
        },
        {
          title: '操作',
          dataIndex: 'operation',
          scopedSlots: { customRender: 'operation' },
          align: 'left',
          width: 230,
        },
        {
          title: '状态',
          key: 'status',
          dataIndex: 'status',
          scopedSlots: { customRender: 'status' },
          width: 160,
        },

      ],
      detailsData: {

      },
      fileData: ''
    };
  },
  computed: {
    // ...mapGetters(['score']),
    // placeholderText() {
    //   return this.searchType === 1
    //     ? '普通搜索只能搜索一个关键词，内容为精准匹配'
    //     : '高级搜索语法：例如( A and B or C )';
    // },
  },

  methods: {

    onSearch() {
      const accountName = this.queryParam.accountName;
      // 验证输入内容
      if (!accountName || (accountName.length < 4 && !this.isChinese(accountName))) {
        this.$message.warning('请输入至少4个英文字符或2个汉字');
        return;
      }


      this.searchLoading = true;



      getCrawl(this.queryParam).then((response) => {

        this.tableData = response.data.rows
        this.total = response.data.total ? Number(response.data.total) : 0
        this.loading = false
        this.tableShow = true;
        this.searchLoading = false;
      }).catch((error) => {
        this.$message.error(error)
        this.searchLoading = false;
      });
    },
    highlightText(text, keyword) {
      if (keyword === '****') return text;
      const escapedKeyword = this.escapeRegExp(keyword);
      const regex = new RegExp(`(${escapedKeyword})`, 'gi');
      return text.replace(regex, '<span class="highlight">$1</span>');
    },

    escapeRegExp(string) {
      return string.replace(/[.*+?^${}()|[\]\\]/g, '\\$&'); // $& 表示整个匹配的字符串
    },

    isChinese(str) {
      let chineseCount = 0;
      for (const char of str) {
        if (/[\u4e00-\u9fa5]/.test(char)) {
          chineseCount++;
          if (chineseCount >= 2) {
            return true;
          }
        }
      }
      return false;
    },

    getTooltipTitle(item) {
      return '查看该内容将扣除1积分' // 默认提示
    },
    getList() {
      this.onSearch()
    },

    onShowSizeChange(current, pageSize) {
      this.queryParam.pageNum = 1;
      this.queryParam.pageSize = pageSize
      this.getList()
    },
    changeSize(current, pageSize) {
      this.queryParam.pageNum = current
      this.queryParam.pageSize = pageSize
      this.getList()
    },

    openDialog(item) {
      details(item._id).then((response) => {
        response.data.content = this.highlightText(response.data.content, this.queryParam.content)
        this.detailsData = response.data;

        this.visible = true;
      }).catch((error) => {
        this.$message.error(error)
      });
    },
    openEditDialog(item) {
      this.searchLoading = true;
      verifyLast(item._id).then((response) => {
        this.detailsData = item;
        this.detailsData.msg = response.msg;
        this.editVisible = true;
        this.searchLoading = false;
      }).catch((error) => {
        this.$message.error(error)
      });





    },
    handleCancel() {
      this.visible = false;
      this.editVisible = false;
      setTimeout(() => {
        this.detailsData = {}
      }, 200);
    },
    handleOk() { },

    /**
     * 更改编辑状态
     *
     * @param type 更改类型
     */
    changeEdit(type) {
      putVerify({ check: type, _id: this.detailsData._id }).then((response) => {
        this.editVisible = false;
        this.getList()
        setTimeout(() => {
          this.detailsData = {}
        }, 200);

      }).catch((error) => {
        this.$message.error(error)
      });
    },

    confirm() {
      // addExamine({ dikName: this.detailsData.filename, dikId: this.detailsData._id }).then((response) => {
      //   this.visible = false;
      //   this.editVisible = false;
      //   this.getList()
      //   setTimeout(() => {
      //     this.detailsData = {}
      //   }, 1500);
      // }).catch((error) => {
      //   this.$message.error(error)
      // });
    },




    handleExport(item) {
      this.download('module/management/downloadFile/resource', {
        _id: item._id
      }, item.filename)
    },

  },

  mounted() {

  },
};
</script>

<style scoped>
.ant-layout-content {
  background: #fff;
  min-height: 82vh;
}

/* Vue 2 使用 /deep/ */
/deep/ .ant-radio-wrapper-checked .ant-radio+span {
  color: #1890FF;
  /* 选中时文字颜色为蓝色 */
}

.ant-btn {
  border-radius: 9999px;
  padding: 6px 30px;
  height: 35px;
  margin: 0 10px;
}


.margin-transition-enter-active,
.margin-transition-leave-active {
  transition: margin-top 15s ease;
  /* 这里是 margin-top 动画的持续时间和过渡曲线 */
}

.margin-transition-enter,
.margin-transition-leave-to {
  margin-top: 20%;
  /* 起始状态 */
}

/deep/.ant-modal-content {
  border-radius: 10px;
}



/* Vue 2 使用 /deep/ */
/deep/ .ant-radio-wrapper-checked .ant-radio+span {
  color: #1890FF;
}

.ant-btn {
  border-radius: 9999px;
  padding: 6px 30px;
  height: 35px;
  margin: 0 10px;
}


/deep/.ant-modal-content {
  border-radius: 10px;
}


/deep/.ant-input-group>.ant-input:first-child,
.ant-input-group-addon:first-child {
  border-top-left-radius: 17px;
  height: 100%;
  border-bottom-left-radius: 17px;
  font-size: 20px;
}

/deep/.ant-btn-lg {
  padding: 0 32px;
  border-top-right-radius: 17px;
  border-bottom-right-radius: 17px;
  height: 100%;
  font-size: 20px;
  font-weight: 700;
}

/deep/.ant-input-group-addon {
  height: 100%;
}

/deep/.ant-input-group {
  height: 55px;
}

.search-icon {
  position: absolute;
  left: 40%;

  top: -180px;
  width: 140px;
  height: 140px;
  margin-bottom: 40px;
  transition: opacity 0.2s ease;
  /* 图片淡入淡出动画 */
}


.bounce-enter-active {
  animation: bounce-in 1s;
}

.bounce-leave-active {
  animation: bounce-in 1s reverse;
}

@keyframes bounce-in {
  0% {
    transform: scale(0);
  }

  50% {
    transform: scale(0.5);
  }

  100% {
    transform: scale(1);
  }
}


.title-box {
  transition: margin-top 1s ease;
  /* 对 margin-top 设置过渡动画 */
  text-align: center;
  animation: 1s fadeIn 1s;
  position: relative;

}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 1.3s ease;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
  /* 图片消失时变为透明 */
}
</style> -->