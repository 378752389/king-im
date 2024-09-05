<script setup>
import CommonList from "@/components/CommonList.vue";
import {useContactsStore} from "@/stores/contacts.js";
import ContactCard from "@/components/ContactCard.vue";
import KingContextMenuItem from "@/components/common/KingContextMenuItem.vue";
import {removeContactAPI} from "@/http/social.js";
import {ShowMessageBox} from "@/components/common/func/messageBox.js";
import {ShowToast} from "@/components/common/func/toast.js";

const contactStore = useContactsStore()
const onContactChange = (contact) => {
  contactStore.setSelectedContact({...contact})
}

const onSearchInput = (text) => {
  // todo
  console.log(text)
}

const onContactSearchClick = () => {
}

const onMarkClick = (contextmenu, close) => {
  console.log(contextmenu)
  ShowToast({
    message: "标记好友功能暂未现实，敬请期待"
  })
  close()
}
const onDeleteContactClick = async (contextmenu, close) => {
  ShowMessageBox({
    message: `请确认是否要删除好友 <span style='color: red;'>${contextmenu.peerMarkname || contextmenu.peerNickname}</span> ?`,
    confirm: async () => {
      const resp = await removeContactAPI({friendId: contextmenu.peerId})
      await contactStore.loadContactList()
      console.log(resp)
      close()
    },
    cancel: () => {
      close()
    }
  })

}
</script>

<template>
  <div class="contact">
    <CommonList :selected-id="contactStore.selectedContactGetter?.id" @item-change="onContactChange"
                :list="contactStore.contactListGetter"
                :render-props="{id: 'id', name: 'peerNickname', avatar: 'peerAvatar'}">
      <template #contextmenu="{contextmenu, close}">
        <KingContextMenuItem @itemClick="onMarkClick(contextmenu, close)" name="标记"/>
        <KingContextMenuItem @itemClick="onDeleteContactClick(contextmenu, close)" name="删除联系人"/>
      </template>
    </CommonList>
    <ContactCard v-if="contactStore.selectedContactGetter != null" :contactInfo="contactStore.selectedContactGetter"/>
  </div>
</template>

<style scoped lang="stylus">
.contact
  display flex
  height 100%

</style>