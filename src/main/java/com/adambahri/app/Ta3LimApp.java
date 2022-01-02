package com.adambahri.app;

import com.adambahri.app.config.ApplicationProperties;
import com.adambahri.app.domain.Resource;
import com.adambahri.app.domain.Subject;
import com.adambahri.app.domain.Topic;
import com.adambahri.app.domain.User;
import com.adambahri.app.domain.UserExtended;
import com.adambahri.app.domain.enumeration.AgeRange;
import com.adambahri.app.domain.enumeration.Children;
import com.adambahri.app.domain.enumeration.CivilStatus;
import com.adambahri.app.domain.enumeration.ResourceType;
import com.adambahri.app.repository.ResourceRepository;
import com.adambahri.app.repository.SubjectRepository;
import com.adambahri.app.repository.TopicRepository;
import com.adambahri.app.repository.UserExtendedRepository;
import com.adambahri.app.repository.UserRepository;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import tech.jhipster.config.DefaultProfileUtil;
import tech.jhipster.config.JHipsterConstants;

@SpringBootApplication
@EnableConfigurationProperties({ LiquibaseProperties.class, ApplicationProperties.class })
public class Ta3LimApp {

    @Autowired
    private ResourceRepository resourcerepo;

    @Autowired
    private UserRepository userrepo;
    
    @Autowired
    private UserExtendedRepository userextendedrepo;
    
    @Autowired
    private SubjectRepository subjectrepo;
    
    @Autowired
    private TopicRepository topicrepo;

    private static final Logger log = LoggerFactory.getLogger(Ta3LimApp.class);

    private final Environment env;

    public Ta3LimApp(Environment env) {
        this.env = env;
    }

    /**
     * Initializes ta3lim.
     * <p>
     * Spring profiles can be configured with a program argument --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on <a href="https://www.jhipster.tech/profiles/">https://www.jhipster.tech/profiles/</a>.
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)
        ) {
            log.error(
                "You have misconfigured your application! It should not run " + "with both the 'dev' and 'prod' profiles at the same time."
            );
        }
        if (
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)
        ) {
            log.error(
                "You have misconfigured your application! It should not " + "run with both the 'dev' and 'cloud' profiles at the same time."
            );
        }
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Ta3LimApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    @Bean
    public CommandLineRunner demo() {
        return args -> {
            // fetch all resources
            log.info("Resources found with findAll():");
            log.info("-------------------------------");
            for (Resource resource : resourcerepo.findAll()) {
                log.info(resource.toString());
            }

            //check if admin exists and set files quota
            User user = userrepo.findOneByLogin("admin").isPresent() ? userrepo.findOneByLogin("admin").get() : null;
            UserExtended userextended = userextendedrepo.findById(1L).isPresent() ? userextendedrepo.findById(1L).get() : null;
            if (user != null && userextended == null) {
            	UserExtended u1 = new UserExtended();
            	u1.setAboutMe("I'm a homeschooling enthousiast, following the unschooling philosophy");
            	u1.setOccupation("IT generalist, Software Developer");
            	u1.setSocialMedia("https://www.adambahri.com");
            	u1.setCivilStatus(CivilStatus.MARRIED);
            	u1.setFirstchild(Children.AGE_04_06);
            	u1.setSecondchild(Children.AGE_07_09);      	
            	u1.setFilesquota(200);
            	u1.setUser(user);
            	userextendedrepo.save(u1);
            }
                                
            //create first 4 subjects
            Subject subject = subjectrepo.findById(1L).isPresent() ? subjectrepo.findById(1L).get() : null;
            String slabel1 = "Arabic language";
            String slabel2 = "Islamic education";
            String slabel3 = "History";
            String slabel4 = "Mathematics";
            if (subject == null) {
            	Subject s1 = new Subject();
            	s1.setLabel(slabel1);
            	s1.setCreationDate(LocalDate.now());
            	subjectrepo.save(s1);
            	Subject s2 = new Subject();
            	s2.setLabel(slabel2);
            	s2.setCreationDate(LocalDate.now());
            	subjectrepo.save(s2);
            	Subject s3 = new Subject();
            	s3.setLabel(slabel3);
            	s3.setCreationDate(LocalDate.now());
            	subjectrepo.save(s3);
            	Subject s4 = new Subject();
            	s4.setLabel(slabel4);
            	s4.setCreationDate(LocalDate.now());
            	subjectrepo.save(s4);
            	}
            
            //create first 4 topics
            Topic topic = topicrepo.findById(1L).isPresent() ? topicrepo.findById(1L).get() : null;
            String tlabel1 = "Reading";
            String tlabel2 = "Writing";
            String tlabel3 = "Arithmetic";
            String tlabel4 = "Morals";
            if (topic == null) {
            	Topic t1 = new Topic();
            	t1.setLabel(tlabel1);
            	t1.setCreationDate(LocalDate.now());
            	topicrepo.save(t1);
            	Topic t2 = new Topic();
            	t2.setLabel(tlabel2);
            	t2.setCreationDate(LocalDate.now());
            	topicrepo.save(t2);
            	Topic t3 = new Topic();
            	t3.setLabel(tlabel3);
            	t3.setCreationDate(LocalDate.now());
            	topicrepo.save(t3);
            	Topic t4 = new Topic();
            	t4.setLabel(tlabel4);
            	t4.setCreationDate(LocalDate.now());
            	topicrepo.save(t4);
            	}
            
            
            //create first 5 resources if user/subject/topic exists in db
            Resource resource = resourcerepo.findById(1L).isPresent() ? resourcerepo.findById(1L).get() : null;
            if (resource == null && user != null && subject != null && topic != null) {
            Resource r1 = new Resource();
            r1.setTitle("TuxMath - free game");
            r1.setActivated(true);
            r1.setDescription("TuxMath is an open source, free game whose difficulty is appropriate for students from elementary to high school, in other words, 7 to 13 years.");
            r1.setCreationDate(LocalDate.now());
            r1.setUser(user);
            r1.setVotes(202L);
            r1.setAngeRage(AgeRange.AGE_07_09);
            r1.setResourceType(ResourceType.URLS);
            r1.setSubject(subjectrepo.findByLabel("Mathematics").get());
            r1.addTopics(topicrepo.findByLabel("Arithmetic").get());
            resourcerepo.save(r1);
            Resource r2 = new Resource();
            r2.setTitle("'Iqra' textbooks");
            r2.setActivated(true);
            r2.setDescription("The first series of Arabic textbooks in Morocco. 'Iqra', published by Ahmed Boukmakh. It consists of 5 volumes and these have been made available for download. Highly recommended for learning to read.");
            r2.setCreationDate(LocalDate.now());
            r2.setUser(user);
            r2.setVotes(57L);
            r2.setAngeRage(AgeRange.AGE_10_12);
            r2.setResourceType(ResourceType.URLS);
            r2.setSubject(subjectrepo.findByLabel("Arabic language").get());
            r2.addTopics(topicrepo.findByLabel("Reading").get());
            r2.addTopics(topicrepo.findByLabel("Writing").get());
            resourcerepo.save(r2);
            Resource r3 = new Resource();
            r3.setTitle("Ibn Battuta, Traveler from Tangier");
            r3.setActivated(true);
            r3.setDescription("In the year 1349 a dusty Arab horseman rode slowly toward the city of Tangier on the North African coast. For Ibn Battuta, it was the end of a long journey.");
            r3.setCreationDate(LocalDate.now());
            r3.setUser(user);
            r3.setVotes(15L);
            r3.setAngeRage(AgeRange.AGE_10_12);
            r3.setResourceType(ResourceType.DOCUMENTS);
            r3.setSubject(subjectrepo.findByLabel("History").get());
            r3.addTopics(topicrepo.findByLabel("Reading").get());
            resourcerepo.save(r3);
            Resource r4 = new Resource();
            r4.setTitle("Taha TV for Kids");
            r4.setActivated(true);
            r4.setDescription("Taha TV for Kids is available on Youtube, it broadcasts from Lebanon and is very suitable for providing an Islamic education to your children.");
            r4.setCreationDate(LocalDate.now());
            r4.setUser(user);
            r4.setVotes(22L);
            r4.setAngeRage(AgeRange.AGE_07_09);
            r4.setResourceType(ResourceType.URLS);
            r4.setSubject(subjectrepo.findByLabel("Islamic education").get());
            r4.addTopics(topicrepo.findByLabel("Morals").get());
            resourcerepo.save(r4);
            Resource r5 = new Resource();
            r5.setTitle("Fable - The Ant and the Grasshopper");
            r5.setActivated(true);
            r5.setDescription("The Ant and the Grasshopper, the fable describes how a hungry grasshopper begs for food from an ant when winter comes. The situation sums up moral lessons about the virtues of hard work.");
            r5.setCreationDate(LocalDate.now());
            r5.setUser(user);
            r5.setVotes(8L);
            r5.setAngeRage(AgeRange.AGE_07_09);
            r5.setResourceType(ResourceType.URLS);
            r5.setSubject(subjectrepo.findByLabel("Arabic language").get());
            r5.addTopics(topicrepo.findByLabel("Reading").get());
            r5.addTopics(topicrepo.findByLabel("Morals").get());
            resourcerepo.save(r5);
            }

        };
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store")).map(key -> "https").orElse("http");
        String serverPort = env.getProperty("server.port");
        String contextPath = Optional
            .ofNullable(env.getProperty("server.servlet.context-path"))
            .filter(StringUtils::isNotBlank)
            .orElse("/");
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info(
            "\n----------------------------------------------------------\n\t" +
            "Application '{}' is running! Access URLs:\n\t" +
            "Local: \t\t{}://localhost:{}{}\n\t" +
            "External: \t{}://{}:{}{}\n\t" +
            "Profile(s): \t{}\n----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles()
        );
    }
}
