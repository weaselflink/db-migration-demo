FROM amazon/aws-cli:2.3.4

COPY pgdg.repo /etc/yum.repos.d/pgdg.repo
RUN yum update -y \
    && yum install -y bash curl postgresql12 jq \
    && yum clean all

COPY dump.sh /aws
WORKDIR /aws

ENTRYPOINT ["sh", "-c", "./dump.sh"]
