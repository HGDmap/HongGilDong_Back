package hongik.map.honggildong.domain.event.repository;

import hongik.map.honggildong.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
