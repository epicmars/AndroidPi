package com.androidpi.data.remote.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jastrelax on 2018/8/13.
 */
public class ResUnsplashPhoto {


    /**
     * id : Qdr4mpifNQQ
     * created_at : 2018-03-19T07:45:24-04:00
     * updated_at : 2018-05-09T05:15:55-04:00
     * width : 5472
     * height : 3648
     * color : #F3E3D2
     * description : null
     * urls : {"raw":"https://images.unsplash.com/photo-1521459893400-80c3a8926f5b?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjMyMDE3fQ&s=ec5ba2979d9ac3b4f372062c91ffff29","full":"https://images.unsplash.com/photo-1521459893400-80c3a8926f5b?ixlib=rb-0.3.5&q=85&fm=jpg&crop=entropy&cs=srgb&ixid=eyJhcHBfaWQiOjMyMDE3fQ&s=b340b9e974abc2e62ef0f5423f7f3727","regular":"https://images.unsplash.com/photo-1521459893400-80c3a8926f5b?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjMyMDE3fQ&s=600db1f8b9761decda8152d8eb1e20a2","small":"https://images.unsplash.com/photo-1521459893400-80c3a8926f5b?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&ixid=eyJhcHBfaWQiOjMyMDE3fQ&s=9441e5151b8a210c7312cd8feba8f824","thumb":"https://images.unsplash.com/photo-1521459893400-80c3a8926f5b?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&ixid=eyJhcHBfaWQiOjMyMDE3fQ&s=089fb3b7813b395520826fc4700cc652"}
     * links : {"self":"https://api.unsplash.com/photos/Qdr4mpifNQQ","html":"https://unsplash.com/photos/Qdr4mpifNQQ","download":"https://unsplash.com/photos/Qdr4mpifNQQ/download","download_location":"https://api.unsplash.com/photos/Qdr4mpifNQQ/download"}
     * categories : []
     * sponsored : false
     * likes : 4
     * liked_by_user : false
     * current_user_collections : []
     * slug : null
     * user : {"id":"MOoBF6DIHSs","updated_at":"2018-06-27T11:18:30-04:00","username":"jamesbold","name":"James Bold","first_name":"James","last_name":"Bold","twitter_username":"iamjamesbold","portfolio_url":"https://smartphotocourses.com","bio":"Photography, writer, creator.","location":null,"links":{"self":"https://api.unsplash.com/users/jamesbold","html":"https://unsplash.com/@jamesbold","photos":"https://api.unsplash.com/users/jamesbold/photos","likes":"https://api.unsplash.com/users/jamesbold/likes","portfolio":"https://api.unsplash.com/users/jamesbold/portfolio","following":"https://api.unsplash.com/users/jamesbold/following","followers":"https://api.unsplash.com/users/jamesbold/followers"},"profile_image":{"small":"https://images.unsplash.com/profile-1492679404723-7d73c381e4a5?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32&s=1e71bf8fbb0228779557cc76d074126a","medium":"https://images.unsplash.com/profile-1492679404723-7d73c381e4a5?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64&s=274a3a90072b1a95ec7e241832abb4e8","large":"https://images.unsplash.com/profile-1492679404723-7d73c381e4a5?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=3d5f7851f2af9c02b21ca37fc40d2c5f"},"instagram_username":"michaelbold1","total_collections":0,"total_likes":5,"total_photos":64}
     * exif : {"make":null,"model":null,"exposure_time":null,"aperture":null,"focal_length":null,"iso":null}
     * views : 351967
     * downloads : 784
     */

    @SerializedName("id")
    private String id;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("width")
    private int width;
    @SerializedName("height")
    private int height;
    @SerializedName("color")
    private String color;
    @SerializedName("description")
    private Object description;
    @SerializedName("urls")
    private UrlsBean urls;
    @SerializedName("links")
    private LinksBean links;
    @SerializedName("sponsored")
    private boolean sponsored;
    @SerializedName("likes")
    private int likes;
    @SerializedName("liked_by_user")
    private boolean likedByUser;
    @SerializedName("slug")
    private Object slug;
    @SerializedName("user")
    private UserBean user;
    @SerializedName("exif")
    private ExifBean exif;
    @SerializedName("views")
    private int views;
    @SerializedName("downloads")
    private int downloads;
    @SerializedName("categories")
    private List<?> categories;
    @SerializedName("current_user_collections")
    private List<?> currentUserCollections;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public UrlsBean getUrls() {
        return urls;
    }

    public void setUrls(UrlsBean urls) {
        this.urls = urls;
    }

    public LinksBean getLinks() {
        return links;
    }

    public void setLinks(LinksBean links) {
        this.links = links;
    }

    public boolean isSponsored() {
        return sponsored;
    }

    public void setSponsored(boolean sponsored) {
        this.sponsored = sponsored;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isLikedByUser() {
        return likedByUser;
    }

    public void setLikedByUser(boolean likedByUser) {
        this.likedByUser = likedByUser;
    }

    public Object getSlug() {
        return slug;
    }

    public void setSlug(Object slug) {
        this.slug = slug;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public ExifBean getExif() {
        return exif;
    }

    public void setExif(ExifBean exif) {
        this.exif = exif;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public List<?> getCategories() {
        return categories;
    }

    public void setCategories(List<?> categories) {
        this.categories = categories;
    }

    public List<?> getCurrentUserCollections() {
        return currentUserCollections;
    }

    public void setCurrentUserCollections(List<?> currentUserCollections) {
        this.currentUserCollections = currentUserCollections;
    }

    public static class UrlsBean {
        /**
         * raw : https://images.unsplash.com/photo-1521459893400-80c3a8926f5b?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjMyMDE3fQ&s=ec5ba2979d9ac3b4f372062c91ffff29
         * full : https://images.unsplash.com/photo-1521459893400-80c3a8926f5b?ixlib=rb-0.3.5&q=85&fm=jpg&crop=entropy&cs=srgb&ixid=eyJhcHBfaWQiOjMyMDE3fQ&s=b340b9e974abc2e62ef0f5423f7f3727
         * regular : https://images.unsplash.com/photo-1521459893400-80c3a8926f5b?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjMyMDE3fQ&s=600db1f8b9761decda8152d8eb1e20a2
         * small : https://images.unsplash.com/photo-1521459893400-80c3a8926f5b?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&ixid=eyJhcHBfaWQiOjMyMDE3fQ&s=9441e5151b8a210c7312cd8feba8f824
         * thumb : https://images.unsplash.com/photo-1521459893400-80c3a8926f5b?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&ixid=eyJhcHBfaWQiOjMyMDE3fQ&s=089fb3b7813b395520826fc4700cc652
         */

        @SerializedName("raw")
        private String raw;
        @SerializedName("full")
        private String full;
        @SerializedName("regular")
        private String regular;
        @SerializedName("small")
        private String small;
        @SerializedName("thumb")
        private String thumb;

        public String getRaw() {
            return raw;
        }

        public void setRaw(String raw) {
            this.raw = raw;
        }

        public String getFull() {
            return full;
        }

        public void setFull(String full) {
            this.full = full;
        }

        public String getRegular() {
            return regular;
        }

        public void setRegular(String regular) {
            this.regular = regular;
        }

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }
    }

    public static class LinksBean {
        /**
         * self : https://api.unsplash.com/photos/Qdr4mpifNQQ
         * html : https://unsplash.com/photos/Qdr4mpifNQQ
         * download : https://unsplash.com/photos/Qdr4mpifNQQ/download
         * download_location : https://api.unsplash.com/photos/Qdr4mpifNQQ/download
         */

        @SerializedName("self")
        private String self;
        @SerializedName("html")
        private String html;
        @SerializedName("download")
        private String download;
        @SerializedName("download_location")
        private String downloadLocation;

        public String getSelf() {
            return self;
        }

        public void setSelf(String self) {
            this.self = self;
        }

        public String getHtml() {
            return html;
        }

        public void setHtml(String html) {
            this.html = html;
        }

        public String getDownload() {
            return download;
        }

        public void setDownload(String download) {
            this.download = download;
        }

        public String getDownloadLocation() {
            return downloadLocation;
        }

        public void setDownloadLocation(String downloadLocation) {
            this.downloadLocation = downloadLocation;
        }
    }

    public static class UserBean {
        /**
         * id : MOoBF6DIHSs
         * updated_at : 2018-06-27T11:18:30-04:00
         * username : jamesbold
         * name : James Bold
         * first_name : James
         * last_name : Bold
         * twitter_username : iamjamesbold
         * portfolio_url : https://smartphotocourses.com
         * bio : Photography, writer, creator.
         * location : null
         * links : {"self":"https://api.unsplash.com/users/jamesbold","html":"https://unsplash.com/@jamesbold","photos":"https://api.unsplash.com/users/jamesbold/photos","likes":"https://api.unsplash.com/users/jamesbold/likes","portfolio":"https://api.unsplash.com/users/jamesbold/portfolio","following":"https://api.unsplash.com/users/jamesbold/following","followers":"https://api.unsplash.com/users/jamesbold/followers"}
         * profile_image : {"small":"https://images.unsplash.com/profile-1492679404723-7d73c381e4a5?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32&s=1e71bf8fbb0228779557cc76d074126a","medium":"https://images.unsplash.com/profile-1492679404723-7d73c381e4a5?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64&s=274a3a90072b1a95ec7e241832abb4e8","large":"https://images.unsplash.com/profile-1492679404723-7d73c381e4a5?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=3d5f7851f2af9c02b21ca37fc40d2c5f"}
         * instagram_username : michaelbold1
         * total_collections : 0
         * total_likes : 5
         * total_photos : 64
         */

        @SerializedName("id")
        private String id;
        @SerializedName("updated_at")
        private String updatedAt;
        @SerializedName("username")
        private String username;
        @SerializedName("name")
        private String name;
        @SerializedName("first_name")
        private String firstName;
        @SerializedName("last_name")
        private String lastName;
        @SerializedName("twitter_username")
        private String twitterUsername;
        @SerializedName("portfolio_url")
        private String portfolioUrl;
        @SerializedName("bio")
        private String bio;
        @SerializedName("location")
        private Object location;
        @SerializedName("links")
        private LinksBeanX links;
        @SerializedName("profile_image")
        private ProfileImageBean profileImage;
        @SerializedName("instagram_username")
        private String instagramUsername;
        @SerializedName("total_collections")
        private int totalCollections;
        @SerializedName("total_likes")
        private int totalLikes;
        @SerializedName("total_photos")
        private int totalPhotos;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getTwitterUsername() {
            return twitterUsername;
        }

        public void setTwitterUsername(String twitterUsername) {
            this.twitterUsername = twitterUsername;
        }

        public String getPortfolioUrl() {
            return portfolioUrl;
        }

        public void setPortfolioUrl(String portfolioUrl) {
            this.portfolioUrl = portfolioUrl;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public Object getLocation() {
            return location;
        }

        public void setLocation(Object location) {
            this.location = location;
        }

        public LinksBeanX getLinks() {
            return links;
        }

        public void setLinks(LinksBeanX links) {
            this.links = links;
        }

        public ProfileImageBean getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(ProfileImageBean profileImage) {
            this.profileImage = profileImage;
        }

        public String getInstagramUsername() {
            return instagramUsername;
        }

        public void setInstagramUsername(String instagramUsername) {
            this.instagramUsername = instagramUsername;
        }

        public int getTotalCollections() {
            return totalCollections;
        }

        public void setTotalCollections(int totalCollections) {
            this.totalCollections = totalCollections;
        }

        public int getTotalLikes() {
            return totalLikes;
        }

        public void setTotalLikes(int totalLikes) {
            this.totalLikes = totalLikes;
        }

        public int getTotalPhotos() {
            return totalPhotos;
        }

        public void setTotalPhotos(int totalPhotos) {
            this.totalPhotos = totalPhotos;
        }

        public static class LinksBeanX {
            /**
             * self : https://api.unsplash.com/users/jamesbold
             * html : https://unsplash.com/@jamesbold
             * photos : https://api.unsplash.com/users/jamesbold/photos
             * likes : https://api.unsplash.com/users/jamesbold/likes
             * portfolio : https://api.unsplash.com/users/jamesbold/portfolio
             * following : https://api.unsplash.com/users/jamesbold/following
             * followers : https://api.unsplash.com/users/jamesbold/followers
             */

            @SerializedName("self")
            private String self;
            @SerializedName("html")
            private String html;
            @SerializedName("photos")
            private String photos;
            @SerializedName("likes")
            private String likes;
            @SerializedName("portfolio")
            private String portfolio;
            @SerializedName("following")
            private String following;
            @SerializedName("followers")
            private String followers;

            public String getSelf() {
                return self;
            }

            public void setSelf(String self) {
                this.self = self;
            }

            public String getHtml() {
                return html;
            }

            public void setHtml(String html) {
                this.html = html;
            }

            public String getPhotos() {
                return photos;
            }

            public void setPhotos(String photos) {
                this.photos = photos;
            }

            public String getLikes() {
                return likes;
            }

            public void setLikes(String likes) {
                this.likes = likes;
            }

            public String getPortfolio() {
                return portfolio;
            }

            public void setPortfolio(String portfolio) {
                this.portfolio = portfolio;
            }

            public String getFollowing() {
                return following;
            }

            public void setFollowing(String following) {
                this.following = following;
            }

            public String getFollowers() {
                return followers;
            }

            public void setFollowers(String followers) {
                this.followers = followers;
            }
        }

        public static class ProfileImageBean {
            /**
             * small : https://images.unsplash.com/profile-1492679404723-7d73c381e4a5?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32&s=1e71bf8fbb0228779557cc76d074126a
             * medium : https://images.unsplash.com/profile-1492679404723-7d73c381e4a5?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64&s=274a3a90072b1a95ec7e241832abb4e8
             * large : https://images.unsplash.com/profile-1492679404723-7d73c381e4a5?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=3d5f7851f2af9c02b21ca37fc40d2c5f
             */

            @SerializedName("small")
            private String small;
            @SerializedName("medium")
            private String medium;
            @SerializedName("large")
            private String large;

            public String getSmall() {
                return small;
            }

            public void setSmall(String small) {
                this.small = small;
            }

            public String getMedium() {
                return medium;
            }

            public void setMedium(String medium) {
                this.medium = medium;
            }

            public String getLarge() {
                return large;
            }

            public void setLarge(String large) {
                this.large = large;
            }
        }
    }

    public static class ExifBean {
        /**
         * make : null
         * model : null
         * exposure_time : null
         * aperture : null
         * focal_length : null
         * iso : null
         */

        @SerializedName("make")
        private Object make;
        @SerializedName("model")
        private Object model;
        @SerializedName("exposure_time")
        private Object exposureTime;
        @SerializedName("aperture")
        private Object aperture;
        @SerializedName("focal_length")
        private Object focalLength;
        @SerializedName("iso")
        private Object iso;

        public Object getMake() {
            return make;
        }

        public void setMake(Object make) {
            this.make = make;
        }

        public Object getModel() {
            return model;
        }

        public void setModel(Object model) {
            this.model = model;
        }

        public Object getExposureTime() {
            return exposureTime;
        }

        public void setExposureTime(Object exposureTime) {
            this.exposureTime = exposureTime;
        }

        public Object getAperture() {
            return aperture;
        }

        public void setAperture(Object aperture) {
            this.aperture = aperture;
        }

        public Object getFocalLength() {
            return focalLength;
        }

        public void setFocalLength(Object focalLength) {
            this.focalLength = focalLength;
        }

        public Object getIso() {
            return iso;
        }

        public void setIso(Object iso) {
            this.iso = iso;
        }
    }
}
