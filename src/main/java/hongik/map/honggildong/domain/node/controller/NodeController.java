package hongik.map.honggildong.domain.node.controller;

import hongik.map.honggildong.domain.node.dto.NodeResponseDTO;
import hongik.map.honggildong.domain.node.service.NodeService;
import hongik.map.honggildong.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("node")
public class NodeController {

    private final NodeService nodeService;

    @GetMapping("/{nodeId}")
    public ApiResponse<NodeResponseDTO.Coordinate> getNode(@PathVariable("nodeId") Long nodeId) {
        NodeResponseDTO.Coordinate body = nodeService.getNodeCoordinate(nodeId);

        return ApiResponse.onSuccess(body);
    }

    @GetMapping("/all")
    public ApiResponse<List<NodeResponseDTO.Coordinate>> getAllNodes() {
        List<NodeResponseDTO.Coordinate> body = nodeService.getAllNodeCoordinates();

        return ApiResponse.onSuccess(body);
    }
}
