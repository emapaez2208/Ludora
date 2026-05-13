package ExperienceGroup.Ludora.features.review;

import ExperienceGroup.Ludora.features.review.domain.dto.ReviewDTORequest;
import ExperienceGroup.Ludora.features.review.domain.dto.ReviewDTOResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reviews")
@AllArgsConstructor
public class ReviewController {

    private IReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDTOResponse> save(@Valid @RequestBody ReviewDTORequest reviewDTORequest){
        ReviewDTOResponse reviewResponse = reviewService.save(reviewDTORequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewResponse);
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<ReviewDTOResponse>> getAllReviewsByUserID(@PathVariable UUID clientId) {
        List<ReviewDTOResponse> reviews = reviewService.getAllReviewsByClientId(clientId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/game/{gameId}/client/{clientId}")
    public ResponseEntity<List<ReviewDTOResponse>> getAllReviewsByGameIdAndClientId(@PathVariable UUID gameId, @PathVariable UUID clientId){
        List<ReviewDTOResponse> reviews = reviewService.getAllReviewsByGameIdAndClientId(gameId, clientId);

        return ResponseEntity.ok(reviews);
    }



    @DeleteMapping("/{reviewID}")
    public ResponseEntity<Void> delete (@PathVariable UUID reviewId){
        reviewService.delete(reviewId);

        return ResponseEntity.noContent().build();
    }

}
