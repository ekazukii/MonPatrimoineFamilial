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
         if(!father.pids.includes(mother.id)){
            father.pids.push(mother.id);
         }
         if(!mother.pids.includes(father.id)){
            mother.pids.push(father.id);
         }
      }
   }
}
export {findParents, addPartnersId}