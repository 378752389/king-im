<script setup>
import CommonList from "@/components/CommonList.vue";
import {useContactsStore} from "@/stores/contacts.js";
import ContactCard from "@/components/ContactCard.vue";
import KingContextMenuItem from "@/components/common/KingContextMenuItem.vue";
import {removeContactAPI} from "@/http/social.js";

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
  close()
}
const onDeleteContactClick = async (contextmenu, close) => {
  const resp = await removeContactAPI({friendId: contextmenu.peerId})
  await contactStore.loadContactList()
  console.log(resp)
  close()
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