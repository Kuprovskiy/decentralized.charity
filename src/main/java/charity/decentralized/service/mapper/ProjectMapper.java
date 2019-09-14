package charity.decentralized.service.mapper;

import charity.decentralized.domain.*;
import charity.decentralized.service.dto.ProjectDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Project} and its DTO {@link ProjectDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    ProjectDTO toDto(Project project);

    @Mapping(source = "userId", target = "user")
    Project toEntity(ProjectDTO projectDTO);

    default Project fromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }
}
