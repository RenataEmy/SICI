package br.ifrn.edu.sici.specifications;

import br.ifrn.edu.sici.enums.Priority;
import br.ifrn.edu.sici.enums.Status;
import br.ifrn.edu.sici.models.Ticket;
import org.springframework.util.ObjectUtils;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;


public class TicketSpec {

    public static Specification<Ticket> statusEquals(Status status) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(status)) {
                return null;
            }
            return builder.equal(root.get("status"), status);
        };
    }

    public static Specification<Ticket> priorityEquals(Priority priority) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(priority)) {
                return null;
            }
            return builder.equal(root.get("priority"), priority);
        };
    }

    public static Specification<Ticket> sectorEquals(Long sectorId) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(sectorId)) {
                return null;
            }
            return builder.equal(root.join("category").join("sector").get("sectorId"), sectorId);
        };
    }


    public static Specification<Ticket> categoryEquals(Long categoryId) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(categoryId)) {
                return null;
            }
            return builder.equal(root.get("category").get("categoryId"), categoryId);
        };
    }

    public static Specification<Ticket> requestEquals(Long requestId) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(requestId)) {
                return null;
            }
            return builder.equal(root.get("request").get("userId"), requestId);
        };
    }

    public static Specification<Ticket> techEquals(Long techId) {
        return (root, query, builder) -> {
            if (ObjectUtils.isEmpty(techId)) {
                return null;
            }
            return builder.equal(root.get("tech").get("userId"), techId);
        };
    }

    public static Specification<Ticket> openDateBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, builder) -> {
            if (start == null && end == null) return null;
            if (start == null) return builder.lessThanOrEqualTo(root.get("openDate"), end);
            if (end == null) return builder.greaterThanOrEqualTo(root.get("openDate"), start);
            return builder.between(root.get("openDate"), start, end);
        };
    }



}
