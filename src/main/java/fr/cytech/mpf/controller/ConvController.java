package fr.cytech.mpf.controller;

import fr.cytech.mpf.dto.MsgAddDTO;
import fr.cytech.mpf.dto.UserGetDTO;
import fr.cytech.mpf.entity.ConvInfo;
import fr.cytech.mpf.dto.MsgGetDTO;
import fr.cytech.mpf.entity.User;
import fr.cytech.mpf.repository.ConvRepository;
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

    ModelMapper modelMapper;

    ConvController() {
        modelMapper = new ModelMapper();
    }

    @PostMapping("/msg")
    public ResponseEntity<MsgAddDTO> addMsg(@RequestBody MsgAddDTO msgDTO) {
        ConvInfo convInfo = modelMapper.map(msgDTO, ConvInfo.class);
        convRepository.save(convInfo);
        return ResponseEntity.ok(msgDTO);
    }
    @GetMapping(value = "/famille")
    public ResponseEntity<List<MsgGetDTO>> getMsgByConvId(@RequestParam Long conv) {
        List<ConvInfo> convInfos = convRepository.findByConv(conv);
        List<MsgGetDTO> msgGetDTOs = convInfos.stream()
                .map(convInfo -> modelMapper.map(convInfo, MsgGetDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(msgGetDTOs);
    }


}
