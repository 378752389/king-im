import {defineStore} from "pinia";
import {computed, ref} from "vue";
import {getGroupListAPI} from "@/http/social.js";
import {genPicture} from "@/utils/picUtils.js";
import {useChatsStore} from "@/stores/chats.js";

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

    const loadGroupList = async () => {
        const groupListData = await getGroupListAPI()
        if (groupListData != null) {
            groupListData.map(group => group.avatar = genPicture(group.name))
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
        groupListGetter, selectedGroupGetter, setSelectedGroup, getGroup, loadGroupList
    }
}, {
    persist: true
})