const findNodeByNodeId = (nodes, nodeId) => {
   return nodes.find(node => node.nodeId === nodeId)
}
export {findNodeByNodeId}