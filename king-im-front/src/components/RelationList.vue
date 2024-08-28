<script setup>
import {reactive, ref, watchEffect} from "vue";
import {addContactAPI, addGroupAPI, applyGroupAPI, socialSearchAPI} from "@/http/social.js";
import defaultIcon from '@/assets/logo.svg'
import {useContactsStore} from "@/stores/contacts.js";
import {useGroupsStore} from "@/stores/groups.js";
import {ShowToast} from "@/components/common/func/toast.js";

const searchForm = reactive({
  type: 1,    // 搜索方式
  text: '',   // 搜索文本
  method: 1,  // 1-联系人搜索，2-群搜索；
})

const searchTypes = ref([
  {
    name: '号码',
    value: 1,
  },
  {
    name: '名称',
    value: 2,
  },
])
const loading = ref(false)
const searchList = ref([])

const onConfirmClick = (searchItem) => {
  console.log(searchItem)
}

const switchSearchMethod = () => {
  if (searchForm.method === 1) {
    searchForm.method = 2
  } else {
    searchForm.method = 1
  }
}

let timer = null

watchEffect(async () => {
  searchForm.text && searchForm.type && searchForm.method
  loading.value = true
  if (timer != null) {
    clearTimeout(timer)
  }
  timer = setTimeout(async () => {
    if (searchForm.text === '') {
      loading.value = false
      return
    }
    try {
      const resp = await socialSearchAPI({
        method: searchForm.method,
        type: searchForm.type,
        text: searchForm.text,
      })

      if (resp != null) {
        if (searchForm.type === 1) {
          searchList.value = resp.filter(item => item.id == searchForm.text)
        } else if (searchForm.type === 2) {
          searchList.value = resp.filter(item => item.name.includes(searchForm.text))
        }
      }
    } finally {
      loading.value = false
    }

  }, 500)
})

const onAddFriendClick = async (searchItem) => {
  const resp = await addContactAPI({
    friendId: searchItem.id
  })
  ShowToast({
    message: "添加好友成功",
    type: "success",
  })

  await useContactsStore().loadContactList()
}

const onApplyGroupClick = async (searchItem) => {
  await applyGroupAPI({
    roomId: searchItem.id
  })
  ShowToast({
    message: "申请成功",
    type: "success"
  })
  await useGroupsStore().loadGroupList()
}
</script>

<template>
  <div class="relation-list">
    <div class="search-box">

      <select v-model="searchForm.type" name="type" class="search-type">
        <option :label="type.name" :value="type.value" :key="type.value" v-for="type in searchTypes"/>
      </select>
      <input v-model="searchForm.text" type="search"/>
      <button class="search-btn" @click="switchSearchMethod">
        {{ searchForm.method === 1 ? '切换 => 群' : '切换 => 联系人' }}
      </button>
    </div>

    <div style="text-align: center; padding: 10px 0;" v-show="loading">loading...</div>

    <div class="search-list">
      <div class="search-item" v-for="searchItem in searchList">
        <img height="60" width="60" :src="searchItem.avatar || defaultIcon" alt=""/>
        <div>{{ searchItem.name }} ( {{ searchItem.id }} )</div>
        <button v-if="!searchItem.bind && searchForm.method === 1" class="confirm-btn"
                @click="onAddFriendClick(searchItem)">加好友
        </button>
        <button v-else-if="!searchItem.bind && searchForm.method === 2" class="confirm-btn"
                @click="onApplyGroupClick(searchItem)">加群
        </button>
        <button disabled v-else class="confirm-btn">{{ searchForm.method === 1 ? '已添加' : '已加群' }}</button>
      </div>
    </div>
  </div>
</template>

<style scoped lang="stylus">
.relation-list
  width 800px

  .search-box
    display flex
    justify-content space-between
    margin-bottom 20px

    .search-type
      border 1px solid rgba(0, 0, 0, 0.2)
      outline none
      width 100px
      margin-right 10px
      padding-left 5px

    input[type=search]
      border 1px solid rgba(0, 0, 0, 0.2)
      outline none
      height 40px
      flex 1
      margin 0 20px 0 0
      padding 0 5px

    .search-btn
      width 120px

  .search-list
    overflow auto
    min-height 300px
    max-height 600px

    .search-item
      border 1px solid rgba(0, 0, 0, 0.1)
      height 70px
      display flex
      align-items center

      img
        margin-right 30px

      div
        flex 1

      .confirm-btn
        width 100px
        padding 0 20px
        height 40px
        margin-right 10px
        justify-self flex-end

</style>