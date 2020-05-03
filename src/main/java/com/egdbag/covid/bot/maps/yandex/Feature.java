package com.egdbag.covid.bot.maps.yandex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Feature
{
    private String type;
    private Geometry geometry;
    private Properties properties;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Geometry
    {
        private String type;
        private List<Double> coordinates;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Double> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<Double> coordinates) {
            this.coordinates = coordinates;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Properties
    {
        private String name;
        private String description;
        @JsonProperty("CompanyMetaData")
        private CompanyMetaData companyMetaData;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public CompanyMetaData getCompanyMetaData() {
            return companyMetaData;
        }

        public void setCompanyMetaData(CompanyMetaData companyMetaData) {
            this.companyMetaData = companyMetaData;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public class CompanyMetaData
        {
            private String name;
            private String address;
            private String url;
            private List<Phone> phones;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public List<Phone> getPhones() {
                return phones;
            }

            public void setPhones(List<Phone> phones) {
                this.phones = phones;
            }

            public class Phone
            {
                private String type;
                private String formatted;

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getFormatted() {
                    return formatted;
                }

                public void setFormatted(String formatted) {
                    this.formatted = formatted;
                }
            }
        }
    }
}
