import {defineStore} from "pinia";
import {computed, ref} from "vue";
import {addGroupAPI, getGroupListAPI} from "@/http/social.js";
import {genPicture} from "@/utils/picUtils.js";
import {useChatsStore} from "@/stores/chats.js";
import {buildNoticeMessage} from "@/utils/msgUtils.js";
import {useUserStore} from "@/stores/user.js";
import {useContactsStore} from "@/stores/contacts.js";

export const useGroupsStore = defineStore('groups', () => {
    const groupList = ref([])
    // 群列表页面中选中的群
    const selectedGroup = ref()

    const groupListGetter = computed(() => {
        return groupList.value;
    })

    const selectedGroupGetter = computed(() => {
        return selectedGroup.value;
    })
    const getGroup = (id) => {
        return groupList.value.find(x => x.roomId === id)
    }

    const setSelectedGroup = (group) => {
        selectedGroup.value = group
    }

    const createGroup = async (members) => {
        const roomMemberList = members.map(member => {
            return {
                id: member.id
            }
        })
        let groupLeaderName = useUserStore().info?.nickName
        let memberNames = members.map(member => member.peerNickname).join(", ")

        let name = "";
        if (memberNames.length > 16) {
            name = memberNames.substring(16) + "..." + "的群聊"
        } else {
            name = memberNames + "的群聊"
        }

        const groupId = await addGroupAPI({
            name,
            roomMemberList,
        })

        let msg = buildNoticeMessage({
            type: 2,
            roomId: groupId,
            fromUid: useUserStore().info?.id,
            content: `${groupLeaderName} 创建了群聊\n，并邀请了 ${memberNames}  加入群聊`
        })
        await loadGroupList()
        useChatsStore().insertMessage(groupId, 2, msg)
    }

    const loadGroupList = async () => {
        const groupListData = await getGroupListAPI()
        if (groupListData != null) {
            groupListData.map(group => {
                if (group.avatar == null) {
                    group.avatar = genPicture(group.name)
                }
                return group;
            })
        }
        groupList.value = groupListData
    }

    const mockInit = () => {
        groupList.value = [
            {
                id: 1,
                userId: 1,
                roomId: 1,
                name: "chat群",
                limit: 200,
                myName: null,
                notice: null,
                markName: "chat群",
                createBy: "king",
                leaderId: 1,
                createTime: null,
                avatar: null,
                roomMemberList: [
                    {
                        id: 1,
                        name: "king",
                        avatar: "https://picsum.photos/512/512?id=1"
                    }
                ]
            },
        ]
    }

    return {
        // groupList,
        // selectedGroup,
        groupListGetter, selectedGroupGetter, createGroup, setSelectedGroup, getGroup, loadGroupList
    }
}, {
    persist: true
})