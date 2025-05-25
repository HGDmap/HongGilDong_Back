package hongik.map.honggildong.domain.event.service;

import hongik.map.honggildong.domain.event.entity.Event;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Override
    public List<Event> getEventListOf() {
        return null;
    }

    @Override
    public List<Event> getEventListOf(double latitude, double longitude) {
        return null;
    }

    @Override
    public  Event getEvent(long eventId) {
        return null;
    }
}
