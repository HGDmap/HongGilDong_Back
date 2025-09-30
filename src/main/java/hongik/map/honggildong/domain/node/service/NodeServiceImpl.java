package hongik.map.honggildong.domain.node.service;


import hongik.map.honggildong.domain.node.converter.NodeConverter;
import hongik.map.honggildong.domain.node.dto.NodeResponseDTO;
import hongik.map.honggildong.domain.node.entity.Node;
import hongik.map.honggildong.domain.node.repository.NodeRepository;
import hongik.map.honggildong.global.apiPayload.code.status.ErrorStatus;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NodeServiceImpl implements NodeService {

    private final NodeRepository nodeRepository;

    @Override
    public NodeResponseDTO.Coordinate getNodeCoordinate(Long nodeId) {

        Node node = nodeRepository.findById(nodeId).orElseThrow(()->new GeneralException(ErrorStatus.NODE_NOT_FOUND));

        return NodeConverter.toCoordinateDTO(node);
    }
}
