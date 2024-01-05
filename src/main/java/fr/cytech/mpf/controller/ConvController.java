package fr.cytech.mpf.controller;

import fr.cytech.mpf.config.MustBeLogged;
import fr.cytech.mpf.dto.*;
import fr.cytech.mpf.entity.CommInfo;
import fr.cytech.mpf.entity.MsgInfo;
import fr.cytech.mpf.entity.User;
import fr.cytech.mpf.repository.CommInfoRepository;
import fr.cytech.mpf.repository.ConvRepository;
import fr.cytech.mpf.repository.NodeRepository;
import fr.cytech.mpf.service.NodeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/conversation")
public class ConvController {
    @Autowired
    ConvRepository convRepository;
    @Autowired
    CommInfoRepository commInfoRepository;
    @Autowired
    NodeRepository nodeRepository;
    @Autowired
    NodeService nodeService;
    ModelMapper modelMapper;

    ConvController() {
        modelMapper = new ModelMapper();
    }

    @MustBeLogged
    @PostMapping("/msg")
    public ResponseEntity<MsgAddDTO> addMsg(@RequestBody MsgAddDTO msgDTO) {
        // Check for null values in required properties
        if (msgDTO.getMessage() == null || msgDTO.getUser_id() == 0 || msgDTO.getDate() == null || msgDTO.getConv() == 0) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }

        MsgInfo msgInfo = modelMapper.map(msgDTO, MsgInfo.class);
        convRepository.save(msgInfo);
        return ResponseEntity.ok(msgDTO);
    }

    @MustBeLogged
    @GetMapping("/msg")
    public ResponseEntity<MsgGetDTO> getMsg(@RequestParam Long id) {
        MsgInfo msgInfo = convRepository.getReferenceById(id);
        MsgGetDTO msgGetDTO = modelMapper.map(msgInfo, MsgGetDTO.class);
        return ResponseEntity.ok(msgGetDTO);
    }

    @MustBeLogged
    @PostMapping(value = "/user")
    public ResponseEntity<List<MsgGetDTO>> getMsgByConvId(@RequestBody Long userId) {
        List<MsgInfo> msgInfos = convRepository.findByConv(userId);
        List<MsgGetDTO> msgGetDTOs = msgInfos.stream()
                .map(msgInfo -> modelMapper.map(msgInfo, MsgGetDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(msgGetDTOs);
    }

    @MustBeLogged
    @PostMapping(value = "/famille")
    public ResponseEntity<List<MsgGetDTO>> getMsgByFamilleUser(@RequestBody Long userId) {
        List<Long> userListConnected = nodeService.findUserAccountIdsByTreeIdAndUserToSearch(userId, userId);

        // Utilisez la méthode findAllByUserAccountIdIn pour récupérer les messages de tous les utilisateurs dans userListConnected
        List<MsgInfo> msgInfos = convRepository.findAllByUser_idIn(userListConnected);

        List<MsgGetDTO> msgGetDTOs = msgInfos.stream()
                .map(msgInfo -> modelMapper.map(msgInfo, MsgGetDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(msgGetDTOs);
    }


    @MustBeLogged
    @GetMapping("/commentary")
    public ResponseEntity<List<CommGetDTO>> getComm(@RequestParam Long conv, @RequestParam Long souvenir) {
        List<CommInfo> commInfos = commInfoRepository.findCommInfoByConvAndSouvenir(conv,souvenir);
        List<CommGetDTO> commGetDTOs = commInfos.stream()
                        .map(commInfo -> modelMapper.map(commInfo, CommGetDTO.class))
                        .collect(Collectors.toList());
                modelMapper.map(commInfos, CommGetDTO.class);
        return ResponseEntity.ok(commGetDTOs);
    }

    @MustBeLogged
    @PostMapping("/commentary")
    public ResponseEntity<CommAddDTO> addComm(@RequestBody CommAddDTO commDTO) {
        CommInfo commInfo = modelMapper.map(commDTO, CommInfo.class);
        commInfoRepository.save(commInfo);
        return ResponseEntity.ok(commDTO);
    }

    @MustBeLogged
    @GetMapping("/test")
    public ResponseEntity<List> addComm(@RequestParam Long userId) {
        List<Long> res = nodeService.findUserAccountIdsByTreeIdAndUserToSearch(userId, userId);
        return ResponseEntity.ok(res);
    }
}
