package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;
import com.amplifyframework.core.model.ModelReference;
import com.amplifyframework.core.model.LoadedModelReferenceImpl;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.core.model.ModelIdentifier;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the UserRating type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserRatings", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
}, hasLazySupport = true)
@Index(name = "undefined", fields = {"id"})
public final class UserRating implements Model {
  public static final UserRatingPath rootPath = new UserRatingPath("root", false, null);
  public static final QueryField ID = field("UserRating", "id");
  public static final QueryField USER = field("UserRating", "userId");
  public static final QueryField RATED_USER = field("UserRating", "ratedId");
  public static final QueryField RATING = field("UserRating", "rating");
  public static final QueryField DESCRIPTION = field("UserRating", "description");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="User") @BelongsTo(targetName = "userId", targetNames = {"userId"}, type = User.class) ModelReference<User> user;
  private final @ModelField(targetType="User") @BelongsTo(targetName = "ratedId", targetNames = {"ratedId"}, type = User.class) ModelReference<User> ratedUser;
  private final @ModelField(targetType="Int", isRequired = true) Integer rating;
  private final @ModelField(targetType="String") String description;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  /** @deprecated This API is internal to Amplify and should not be used. */
  @Deprecated
   public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public ModelReference<User> getUser() {
      return user;
  }
  
  public ModelReference<User> getRatedUser() {
      return ratedUser;
  }
  
  public Integer getRating() {
      return rating;
  }
  
  public String getDescription() {
      return description;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private UserRating(String id, ModelReference<User> user, ModelReference<User> ratedUser, Integer rating, String description) {
    this.id = id;
    this.user = user;
    this.ratedUser = ratedUser;
    this.rating = rating;
    this.description = description;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserRating userRating = (UserRating) obj;
      return ObjectsCompat.equals(getId(), userRating.getId()) &&
              ObjectsCompat.equals(getUser(), userRating.getUser()) &&
              ObjectsCompat.equals(getRatedUser(), userRating.getRatedUser()) &&
              ObjectsCompat.equals(getRating(), userRating.getRating()) &&
              ObjectsCompat.equals(getDescription(), userRating.getDescription()) &&
              ObjectsCompat.equals(getCreatedAt(), userRating.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userRating.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUser())
      .append(getRatedUser())
      .append(getRating())
      .append(getDescription())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserRating {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("user=" + String.valueOf(getUser()) + ", ")
      .append("ratedUser=" + String.valueOf(getRatedUser()) + ", ")
      .append("rating=" + String.valueOf(getRating()) + ", ")
      .append("description=" + String.valueOf(getDescription()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static RatingStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static UserRating justId(String id) {
    return new UserRating(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      user,
      ratedUser,
      rating,
      description);
  }
  public interface RatingStep {
    BuildStep rating(Integer rating);
  }
  

  public interface BuildStep {
    UserRating build();
    BuildStep id(String id);
    BuildStep user(User user);
    BuildStep ratedUser(User ratedUser);
    BuildStep description(String description);
  }
  

  public static class Builder implements RatingStep, BuildStep {
    private String id;
    private Integer rating;
    private ModelReference<User> user;
    private ModelReference<User> ratedUser;
    private String description;
    public Builder() {
      
    }
    
    private Builder(String id, ModelReference<User> user, ModelReference<User> ratedUser, Integer rating, String description) {
      this.id = id;
      this.user = user;
      this.ratedUser = ratedUser;
      this.rating = rating;
      this.description = description;
    }
    
    @Override
     public UserRating build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserRating(
          id,
          user,
          ratedUser,
          rating,
          description);
    }
    
    @Override
     public BuildStep rating(Integer rating) {
        Objects.requireNonNull(rating);
        this.rating = rating;
        return this;
    }
    
    @Override
     public BuildStep user(User user) {
        this.user = new LoadedModelReferenceImpl<>(user);
        return this;
    }
    
    @Override
     public BuildStep ratedUser(User ratedUser) {
        this.ratedUser = new LoadedModelReferenceImpl<>(ratedUser);
        return this;
    }
    
    @Override
     public BuildStep description(String description) {
        this.description = description;
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, ModelReference<User> user, ModelReference<User> ratedUser, Integer rating, String description) {
      super(id, user, ratedUser, rating, description);
      Objects.requireNonNull(rating);
    }
    
    @Override
     public CopyOfBuilder rating(Integer rating) {
      return (CopyOfBuilder) super.rating(rating);
    }
    
    @Override
     public CopyOfBuilder user(User user) {
      return (CopyOfBuilder) super.user(user);
    }
    
    @Override
     public CopyOfBuilder ratedUser(User ratedUser) {
      return (CopyOfBuilder) super.ratedUser(ratedUser);
    }
    
    @Override
     public CopyOfBuilder description(String description) {
      return (CopyOfBuilder) super.description(description);
    }
  }
  

  public static class UserRatingIdentifier extends ModelIdentifier<UserRating> {
    private static final long serialVersionUID = 1L;
    public UserRatingIdentifier(String id) {
      super(id);
    }
  }
  
}
