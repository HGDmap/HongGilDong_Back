package hongik.map.honggildong.domain.direction.service;

import hongik.map.honggildong.domain.direction.dto.DirectionResponseDTO;
import hongik.map.honggildong.domain.direction.service.pathfinding.AstarAlgorithm;
import hongik.map.honggildong.domain.node.entity.Node;
import hongik.map.honggildong.domain.node.repository.NodeRepository;
import hongik.map.honggildong.global.apiPayload.code.status.ErrorStatus;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectionServiceImpl implements DirectionService {

    private final AstarAlgorithm astarAlgorithm;
    private final NodeRepository nodeRepository;

    @Override
    public DirectionResponseDTO getDirection(Long from, Long to) {
        List<Long> nodeIds = astarAlgorithm.findPath(from, to);
        List<DirectionResponseDTO.PathNodes> pathNodes = new ArrayList<>(nodeIds.size());

        for (Long nodeId : nodeIds) {
            Node node = nodeRepository.findById(nodeId).orElseThrow(() -> new GeneralException(ErrorStatus.NODE_NOT_FOUND));
            pathNodes.add(
                    DirectionResponseDTO.PathNodes.builder()
                            .nodeId(node.getId())
                            .nodeName(node.getName())
                            .nodeCode(node.getCode())
                            .latitude(node.getLatitude())
                            .longitude(node.getLongitude())
                            .image(node.getMainImg())
                            .build()
            );
        }
        double time = astarAlgorithm.findPathWithTime(from, to);
        int minute = (int) (time / 60);
        int seconds = (int) (time % 60);
        return new DirectionResponseDTO(minute, seconds, pathNodes);
    }
}
