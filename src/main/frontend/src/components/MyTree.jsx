import React, {useState, useEffect, useRef} from 'react';
import FamilyTree from "@balkangraph/familytree.js";
import TreeSettings from "./TreeSettings.jsx";


export default function Chart({nodes, readOnly, treeId}) {
    const [defModalItem, setDefModalItem] = useState();

    const ref = useRef();

    useEffect(() => {
        const nodesCopy = structuredClone(nodes)

        FamilyTree.templates.customTommyTemplate = Object.assign({}, FamilyTree.templates.tommy);
        FamilyTree.templates.customTommyTemplate.menuButton =
            '<div style="position:absolute;right:{p}px;top:{p}px; width:40px;height:30px;cursor:pointer;" data-ctrl-menu="">'
            + '<img width="38px" src="https://www.svgrepo.com//show/13688/settings.svg"  alt="MY IMAGE"/>'
            + '</div>';

        FamilyTree.templates.customTommyTemplate_male = Object.assign({}, FamilyTree.templates.customTommyTemplate);
        FamilyTree.templates.customTommyTemplate_female = Object.assign({}, FamilyTree.templates.customTommyTemplate);

        FamilyTree.templates.registered_female = Object.assign({}, FamilyTree.templates.tommy);
        FamilyTree.templates.registered_female.node =
            '<rect x="0" y="0" height="{h}" width="{w}" stroke-width="1" fill="#F47D01" stroke="#aeaeae" rx="7" ry="7"></rect>' +
            '<svg id="custom-star" width="330" height="100" viewBox="-180 -10 180 180"> <path style="fill:#FFFF00;" d="M26.285,2.486l5.407,10.956c0.376,0.762,1.103,1.29,1.944,1.412l12.091,1.757 c2.118,0.308,2.963,2.91,1.431,4.403l-8.749,8.528c-0.608,0.593-0.886,1.448-0.742,2.285l2.065,12.042 c0.362,2.109-1.852,3.717-3.746,2.722l-10.814-5.685c-0.752-0.395-1.651-0.395-2.403,0l-10.814,5.685 c-1.894,0.996-4.108-0.613-3.746-2.722l2.065-12.042c0.144-0.837-0.134-1.692-0.742-2.285l-8.749-8.528 c-1.532-1.494-0.687-4.096,1.431-4.403l12.091-1.757c0.841-0.122,1.568-0.65,1.944-1.412l5.407-10.956 C22.602,0.567,25.338,0.567,26.285,2.486z"/> </svg>';

        const editForm = {
            saveAndCloseBtn: "Sauvegarder",
            cancelBtn: "Annuler",
            addMore: null,
            generateElementsFromFields: false,
            elements: [
                {type: 'textbox', label: 'Nom complet', binding: 'name'},
                {type: 'textbox', label: 'Date de naissance', binding: 'birthday'},
                {type: 'textbox', label: 'Sexe', binding: 'gender'}
            ],
            buttons: {
                pdf: null,
                share: null,
            }
        }

        if(readOnly) editForm.buttons.edit = null;

        const family = new FamilyTree (ref.current , {
            template: "customTommyTemplate",
            nodesCopy,
            tags: {
                registered: {
                    template: "registered_female"
                }
            },
            enableSearch: false,
            nodeTreeMenu: readOnly ? undefined : true,
            editForm,
            nodeBinding: {
                field_0: 'name',
                field_2: 'birthday',
            },
            menu: readOnly ? null : {}
        });

        if(!readOnly) {
            family.menuUI.on('show', function(sender, args){
                args.menu = {
                    stats: { text: "Statistiques", onClick: function() {setDefModalItem("stats")} },
                    demandes: { text: "Demandes", onClick: function () {setDefModalItem("demands")} },
                }
            });
        }
        
    }, [nodes]);

    return (
        <>
            <div id="tree" ref={ref}></div>
            <TreeSettings id={treeId} opened={defModalItem !== undefined} defItem={defModalItem} handleClose={() => setDefModalItem(undefined)}/>
        </>
    );
}
