package ExperienceGroup.Ludora.features.review;

import ExperienceGroup.Ludora.features.review.domain.ReviewEntity;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reviews")

@AllArgsConstructor
public class ReviewController {

    @GetMapping
    public List<ReviewEntity> findAllByGame(@RequestParam UUID gameId) {
        System.out.println("Obteniendo reviews del juego de ID: " + gameId);


    }
}
