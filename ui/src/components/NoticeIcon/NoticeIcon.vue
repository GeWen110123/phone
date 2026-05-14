<template>
  <div>
    <a-popover
      v-model="visible"
      trigger="click"
      placement="bottomRight"
      overlayClassName="header-notice-wrapper"
      :getPopupContainer="() => $refs.noticeRef.parentElement"
      :autoAdjustOverflow="true"
      :arrowPointAtCenter="true"
      :overlayStyle="{ width: '300px', top: '50px' }"
    >
      <template slot="content">
        <a-spin :spinning="loading">
          <a-tabs v-model="queryParam.type" @change="changeType">
            <a-tab-pane v-for="d in typeOptions" :key="d.dictValue" :tab="d.dictLabel">
              <a-list style="max-height: 300px; overflow: hidden">
                <div
                  slot="loadMore"
                  :style="{ textAlign: 'center', marginTop: '12px', height: '32px', lineHeight: '32px' }"
                >
                  <a-button v-if="list.length > 3" @click="onLoadMore"> 查看所有 </a-button>
                </div>
                <a-list-item v-for="(item, index) in list.slice(0, 3)" :key="index">
                  <a-list-item-meta :description="item.createTime">
                    <a slot="title" @click="$refs.noticeDetail.getNotice(item.message.noticeId, item.id)">
                      <ellipsis :length="32" tooltip>{{ item.message.noticeTitle }}</ellipsis>

                      <span v-if="item.isRead == 0" style="float: right; color: red; font-size: 12px"> 未读 </span>
                      <span v-if="item.isRead == 1" style="float: right; color: #ccc; font-size: 12px"> 已读 </span>
                    </a>
                  </a-list-item-meta>
                </a-list-item>
              </a-list>
            </a-tab-pane>
          </a-tabs>
        </a-spin>
      </template>
      <span @click="fetchNotice" class="header-notice" ref="noticeRef">
        <a-icon :count="1" style="font-size: 20px" type="bell" />
        <a-badge class="ige" :count="titCount" />
      </span>
    </a-popover>
    <notice-detail ref="noticeDetail" :typeOptions="typeOptions" />
  </div>
</template>

<script>
import { listNotice } from '@/api/system/notice'
import Ellipsis from '@/components/Ellipsis'
import NoticeDetail from './NoticeDetail'
import { mapGetters } from 'vuex'

export default {
  name: 'HeaderNotice',
  components: {
    Ellipsis,
    NoticeDetail,
  },
  data() {
    return {
      loading: false,
      visible: false,
      queryParam: {
        pageNum: 1,
        pageSize: 5,
        status: 0,
        type: '1',
      },
      list: [],
      typeOptions: [],
    }
  },
  methods: {
    getList() {
      this.loading = true
      listNotice(this.queryParam).then((response) => {
        response.rows.forEach((item) => {
          item.message = JSON.parse(item.message)
        })
        response.rows.sort((a, b) => {
          return a.isRead - b.isRead
        })
        this.list = this.list.concat(response.rows)
        this.total = response.total

        this.loading = false
      })
    },
    fetchNotice() {
      this.resetQuery()
      if (!this.visible) {
        if (this.typeOptions.length === 0) {
          this.getDicts('sys_notice_type').then((response) => {
            this.typeOptions = response.data
          })
        }
        this.getList()
      }
      this.visible = !this.visible
    },
    resetQuery() {
      this.queryParam = {
        pageNum: 1,
        pageSize: 5,
        status: 0,
        type: '1',
      }
      this.list = []
    },
    changeType(key) {
      this.resetQuery()
      this.queryParam.type = key
      this.getList()
    },
    onLoadMore() {
      if (this.user.roles[0].roleSort == 2) {
        // this.$router.push({ path: '/noticeUser' })
        const herf = this.$router.resolve({
          path: '/noticeUser',
        })
        window.open(herf.href, '_blank')
      } else {
        // this.$router.push({ path: '/system/notice' })

        const herf = this.$router.resolve({
          path: '/notice',
        })
        window.open(herf.href, '_blank')
      }
    },
  },
  computed: {
    ...mapGetters(['titCount', 'user']),
  },
}
</script>

<style lang="css">
.header-notice-wrapper {
  top: 50px !important;
}
</style>
<style lang="less" scoped>
.header-notice {
  display: inline-block;
  transition: all 0.3s;
  position: relative;
  cursor: pointer;
  span {
    vertical-align: initial;
  }

  .ige {
    position: absolute;
    left: 10px;
    top: 6px;
  }
}
</style>
