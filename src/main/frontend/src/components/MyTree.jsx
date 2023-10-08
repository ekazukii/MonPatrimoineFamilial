import React, {useState, useEffect, useRef} from 'react';
import FamilyTree from "@balkangraph/familytree.js";


export default function Chart({nodes}) {
    const [familyObject, setFamilyObject] = useState();

    const ref = useRef();

    useEffect(() => {
        if(familyObject) {
            return familyObject.load(nodes);
        }

        const family = new FamilyTree (ref.current , {
            nodes,
            enableSearch: false,
            nodeTreeMenu: true,
            editForm: {
                saveAndCloseBtn: "Sauvegarder",
                cancelBtn: "Annuler",
                addMore: null,
                generateElementsFromFields: false,
                elements: [
                    {type: 'textbox', label: 'Nom complet', binding: 'name'},
                    {type: 'textbox', label: 'Date de naissance', binding: 'birthday'},
                ],
                buttons: {
                    pdf: null,
                    share: null,
                }
            },
            nodeBinding: {
                field_0: 'name',
                field_2: 'birthday'
            }
        });
        setFamilyObject(family)
    }, [nodes]);

    return (
        <div id="tree" ref={ref}></div>
    );
}
