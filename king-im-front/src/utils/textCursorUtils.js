/**
 * 获取 root 节点下最后一个文本节点
 * @param focusNode
 * @returns {*}
 */
export const getLastTextNode = (focusNode) => {
    if (focusNode.nodeType === Node.TEXT_NODE) {
        return focusNode
    } else if (focusNode.nodeType === Node.ELEMENT_NODE) {
        getLastTextNode(focusNode.lastChild)
    }
}

/**
 * 获取光标所在位置下 root 节点内的子节点
 * @param rootNode
 * @returns {*}
 */
export const getCursorNode = (rootNode) => {
    let sel = document.getSelection()
    let focusNode = sel.focusNode
    let focusOffset = sel.focusOffset
    if (!rootNode.contains(focusNode)) {
        throw new Error("当前选定光标不在root节点内")
    }
    if (focusNode === rootNode) {
        let nthChild = focusNode.childNodes[focusOffset]
        if (nthChild) {
            return nthChild
        }
        return
    }
    return recurFindNode(rootNode, focusNode)
}

const recurFindNode = (rootNode, findNode) => {
    if (findNode.parentNode === rootNode) {
        return findNode
    }
    return recurFindNode(rootNode, findNode.parentNode)
}


const setNodeLastPosition = (node) => {
    let sel = document.getSelection()
    let range = sel.getRangeAt(0)
    let len = 0
    if (node.nodeType === Node.ELEMENT_NODE) {
        let childNodes = node.childNodes
        len = childNodes.length
    } else if (node.nodeType === Node.TEXT_NODE) {
        let content = node.textContent
        len = content.length
    } else {
        console.log("节点类型不确定")
    }
    range.setEnd(node, len)
    range.collapse()
}

export const deleteOne = (rootNode) => {
    let removeEmptyNode = false
    const node = getCursorNode(rootNode)
    // console.log(node)
    if (node == null) {
        return
    }
    if (node.nodeType === Node.ELEMENT_NODE) {
        let preNode = node.previousSibling
        rootNode.removeChild(node)
        console.log("移除了元素节点", node)
        if (preNode) {
            setNodeLastPosition(preNode)
            if (node.nodeName === 'BR') {
                deleteOne(rootNode)
            }
        }
    } else if (node.nodeType === Node.TEXT_NODE) {
        let content = node.textContent
        let preNode = node.previousSibling
        let sel = document.getSelection()
        let range = sel.getRangeAt(0)

        if (range.endOffset > 0) {
            range.setStart(node, range.endOffset - 1)
            range.setEnd(node, range.endOffset)
            let content = range.extractContents()
            // range.deleteContents()
            console.log("删除的文本内容：", content.textContent)
            range.collapse()
        } else {
            if (content.length === 0) {
                console.log("移除了空文本节点")
                node.parentNode.removeChild(node)
                removeEmptyNode = true
            }
            if (preNode) {
                console.log("重置结束光标：", preNode)
                setNodeLastPosition(preNode)
                if (removeEmptyNode) {
                    deleteOne(rootNode)
                }
            }
        }


        // if (content.length > 1) {
        //
        // } else {
        //     let preNode = node.previousSibling
        //     rootNode.removeChild(node)
        //     if (preNode) {
        //         range.setEndAfter(preNode)
        //         range.collapse()
        //     }
        //
        // }
    }
}