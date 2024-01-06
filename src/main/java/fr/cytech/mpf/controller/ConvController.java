package fr.cytech.mpf.controller;

import fr.cytech.mpf.config.MustBeLogged;
import fr.cytech.mpf.dto.*;
import fr.cytech.mpf.entity.CommInfo;
import fr.cytech.mpf.entity.MsgInfo;
import fr.cytech.mpf.entity.User;
import fr.cytech.mpf.repository.CommInfoRepository;
import fr.cytech.mpf.repository.ConvRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MVC Controller for the conversation / memories features
 */
@Controller
@RequestMapping("/conversation")
public class ConvController {
    @Autowired
    ConvRepository convRepository;
    @Autowired
    CommInfoRepository commInfoRepository;
    ModelMapper modelMapper;

    ConvController() {
        modelMapper = new ModelMapper();
    }

    /**
     * Send a new messahe / memories in the specified conversation
     * @param msgDTO data of the message
     * @return HTTP Code 400 if body is malformed 200 otherwise
     */
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

    /**
     * Get a specific message from the messageId
     * @param id id of the conversation
     * @return HTTP Code 400 if body is malformed 200 otherwise
     */
    @MustBeLogged
    @GetMapping("/msg")
    public ResponseEntity<MsgGetDTO> getMsg(@RequestParam Long id) {
        MsgInfo msgInfo = convRepository.getReferenceById(id);
        MsgGetDTO msgGetDTO = modelMapper.map(msgInfo, MsgGetDTO.class);
        return ResponseEntity.ok(msgGetDTO);
    }

    /**
     * Fetch all messages in the specific conversation
     * @param conv Id of the conversation
     * @return HTTP Code 400 if body is malformed 200 otherwise, will return a list of MsgGetDTO
     */
    @MustBeLogged
    @GetMapping(value = "/famille")
    public ResponseEntity<List<MsgGetDTO>> getMsgByConvId(@RequestParam Long conv) {
        List<MsgInfo> msgInfos = convRepository.findByConv(conv);
        List<MsgGetDTO> msgGetDTOs = msgInfos.stream()
                .map(msgInfo -> modelMapper.map(msgInfo, MsgGetDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(msgGetDTOs);
    }

    /**
     * Fetch all comments from a specific conversation and post
     * @param conv Id of the conversatio
     * @param souvenir Id of the memories/message
     * @return HTTP Code 400 if body is malformed 200 otherwise, will return a list of CommGetDTO
     */
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

    /**
     * Add a comment on a specific post of a conversation
     * @param commDTO Comment object
     * @return HTTP Code 400 if body is malformed 200 otherwise
     */
    @MustBeLogged
    @PostMapping("/commentary")
    public ResponseEntity<CommAddDTO> addComm(@RequestBody CommAddDTO commDTO) {
        CommInfo commInfo = modelMapper.map(commDTO, CommInfo.class);
        commInfoRepository.save(commInfo);
        return ResponseEntity.ok(commDTO);
    }
}
