package fr.cytech.mpf.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class NodeEditDTO extends NodeAddDTO {
    public UUID id;
}
