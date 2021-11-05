FROM alpine:3.11

RUN apk -v --update add \
        bash \
        curl \
        postgresql \
        jq \
        python \
        py-pip \
        groff \
        less \
        mailcap \
        && \
    pip install --upgrade awscli==1.14.5 s3cmd==2.0.1 python-magic && \
    apk -v --purge del py-pip && \
    rm /var/cache/apk/*

VOLUME /root/.aws
VOLUME /project
WORKDIR /project

COPY dump.sh /project


ENTRYPOINT ["sh", "-c", "./dump.sh"]
