package ExperienceGroup.Ludora.features.review;

import ExperienceGroup.Ludora.features.review.domain.dto.ReviewDTORequest;
import ExperienceGroup.Ludora.features.review.domain.dto.ReviewDTOResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/reviews")
@AllArgsConstructor
@Tag(name = "Reviews", description = "Endpoints for managing game reviews, ratings, user scores, and written feedback")
public class ReviewController {

    private final IReviewService reviewService;

    @Operation(
            summary = "Post a new review",
            description = "Registers a new game review containing score ratings and written feedback, binding it to the submitting client profile."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review posted successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid payload structure or input data constraints validation failed.", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ReviewDTOResponse> save(@Valid @RequestBody ReviewDTORequest reviewDTORequest){
        ReviewDTOResponse reviewResponse = reviewService.save(reviewDTORequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewResponse);
    }

    @Operation(
            summary = "List reviews with advanced filters",
            description = "Retrieves a list of reviews matching multiple optional parameters like score thresholds or creation timestamps."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered list of reviews retrieved successfully.")
    })
    @GetMapping
    public ResponseEntity<List<ReviewDTOResponse>> getAllReviews(
            @Parameter(description = "Filter by associated video game UUID") @RequestParam(required = false) UUID gameId,
            @Parameter(description = "Filter by reviewer client UUID") @RequestParam(required = false) UUID clientId,
            @Parameter(description = "Minimum rating value boundary") @RequestParam(required = false) Integer minRating,
            @Parameter(description = "Maximum rating value boundary") @RequestParam(required = false) Integer maxRating,
            @Parameter(description = "Initial timestamp boundary (ISO format: YYYY-MM-DDTHH:mm:ss)") @RequestParam(required = false) LocalDateTime minDate,
            @Parameter(description = "Final timestamp boundary (ISO format: YYYY-MM-DDTHH:mm:ss)") @RequestParam(required = false) LocalDateTime maxDate) {

        List<ReviewDTOResponse> reviews = reviewService.getAllReviews(gameId, clientId, minRating, maxRating, minDate, maxDate);
        return ResponseEntity.ok(reviews);
    }

    @Operation(
            summary = "Get reviews by game ID",
            description = "Retrieves all user reviews and ratings submitted for a specific game structure using its unique UUID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game reviews list retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "No game found matching the provided UUID parameter.", content = @Content)
    })
    @GetMapping("/games/{gameId}")
    public ResponseEntity<List<ReviewDTOResponse>> getAllReviewsByGameID(@Parameter(description = "Unique UUID of the game", required = true) @PathVariable UUID gameId) {
        List<ReviewDTOResponse> reviews = reviewService.getAllReviewsByGameId(gameId);
        return ResponseEntity.ok(reviews);
    }

    @Operation(
            summary = "Get reviews written by a client",
            description = "Retrieves the full historical profile log of reviews submitted by a specific client using their UUID tracker."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client historical reviews log retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "No client found matching the provided UUID specification.", content = @Content)
    })
    @GetMapping("/clients/{clientId}")
    public ResponseEntity<List<ReviewDTOResponse>> getAllReviewsByClientID(@Parameter(description = "Unique UUID of the client author", required = true) @PathVariable UUID clientId) {
        List<ReviewDTOResponse> reviews = reviewService.getAllReviewsByClientId(clientId);
        return ResponseEntity.ok(reviews);
    }

    @Operation(
            summary = "Get exact review details by game and client parameters",
            description = "Fetches the specific review entries posted by a distinct client user regarding a targeted game profile."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Target specific review log profile found successfully.")
    })

    @GetMapping("/games/{gameId}/client/{clientId}")
    public ResponseEntity<List<ReviewDTOResponse>> getAllReviewsByGameIdAndClientId(@Parameter(description = "Target game profile UUID", required = true) @PathVariable UUID gameId,
                                                                                    @Parameter(description = "Target author client UUID", required = true) @PathVariable UUID clientId){
        List<ReviewDTOResponse> reviews = reviewService.getAllReviewsByGameIdAndClientId(gameId, clientId);

        return ResponseEntity.ok(reviews);
    }

    @Operation(
            summary = "Delete review log entry",
            description = "Permanently drops a review entry from the database system registry using its unique tracking UUID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Review log entry deleted successfully (No Content)."),
            @ApiResponse(responseCode = "404", description = "No review data found matching the given UUID description.", content = @Content)
    })

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> delete (@Parameter(description = "Unique UUID of the target review to purge", required = true) @PathVariable UUID reviewId){
        reviewService.delete(reviewId);

        return ResponseEntity.noContent().build();
    }

}
