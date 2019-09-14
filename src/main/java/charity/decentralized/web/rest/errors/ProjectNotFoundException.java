package charity.decentralized.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class ProjectNotFoundException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public ProjectNotFoundException() {
        super(ErrorConstants.EMAIL_NOT_FOUND_TYPE, "Project not fond exception", Status.BAD_REQUEST);
    }
}
