package pl.trimlogic.restapi.config.sources;

public interface AppConfig {

    Object getRequiredProperty(String key);
}
