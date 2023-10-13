const findParents = (nodes, mid, fid) => {
   let father, mother;
   for(const node of nodes) {
      if(node.id === mid) {
         mother = node;
      } else if(node.id === fid) {
         father = node;
      }
   }

   return [father, mother];
}

const addPartnersId = (nodes) => {
   for(const node of nodes) {
      if(node.mid && node.fid) {
         const [father, mother] = findParents(nodes, node.mid, node.fid);
         father.pids.push(mother.id);
         mother.pids.push(father.id);
      }
   }
}
export {findParents, addPartnersId}