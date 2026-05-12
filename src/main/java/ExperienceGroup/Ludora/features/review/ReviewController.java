package ExperienceGroup.Ludora.features.review;

import ExperienceGroup.Ludora.features.review.domain.dto.ReviewDTORequest;
import ExperienceGroup.Ludora.features.review.domain.dto.ReviewDTOResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reviews")
@AllArgsConstructor
public class ReviewController {

    private IReviewService reviewService;

    @GetMapping("/game/{gameID}")
    ResponseEntity<List<ReviewDTOResponse>> getAllReviewsByUserID(@PathVariable UUID userID) {
        return ResponseEntity.ok(reviewService.getAllReviewsByUserID(userID));
    }

    @PostMapping
    ReviewDTOResponse save(@RequestBody ReviewDTORequest reviewDTORequest){
        ReviewDTOResponse reviewDTOResponse = reviewService.save(reviewDTORequest);
        return reviewDTOResponse;
    }

    List<ReviewDTOResponse> getAllReviewsByGameID(UUID gameID);

    List<ReviewDTOResponse> getAllReviewsByGameIDAndUserID(UUID gameID, UUID id);

    @DeleteMapping("{reviewID}")
    void delete(UUID reviewID){};

}
