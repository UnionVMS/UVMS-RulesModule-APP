create sequence hibernate_sequence start with 1 increment by 1;

    create table action (
        action_id bigint not null,
        action_action varchar(255),
        action_order integer,
        action_value varchar(255),
        action_rule_id bigint,
        primary key (action_id)
    );

    create table alarmitem (
        alarmitem_id bigint not null,
        alarmitem_guid varchar(255),
        alarmitem_ruleguid varchar(255),
        alarmitem_rulename varchar(255),
        alarmitem_updattim timestamp,
        alarmitem_upuser varchar(255),
        alarmitem_alarmrep_id bigint,
        primary key (alarmitem_id)
    );

    create table alarmreport (
        alarmrep_id bigint not null,
        alarmrep_assetguid varchar(255),
        alarmrep_createddate timestamp,
        alarmrep_guid varchar(255),
        alarmrep_plugintype varchar(255),
        alarmrep_recipient varchar(255),
        alarmrep_status varchar(255),
        alarmrep_updattim timestamp,
        alarmrep_upuser varchar(255),
        primary key (alarmrep_id)
    );

    create table context_expression (
        id bigint not null,
        context varchar(255),
        expression varchar(255) not null,
        failure_message varchar(255),
        rule_id bigint,
        primary key (id)
    );

    create table customrule (
        rule_id bigint not null,
        rule_active boolean,
        rule_archived boolean,
        rule_availability varchar(255),
        rule_description varchar(255),
        rule_enddate timestamp,
        rule_guid varchar(255),
        rule_name varchar(255),
        rule_organisation varchar(255),
        rule_startdate timestamp,
        rule_lasttriggered timestamp,
        rule_updattim timestamp,
        rule_upuser varchar(255),
        primary key (rule_id)
    );

    create table FADocumentID (
        id bigint not null,
        created_on timestamp not null,
        type varchar(255),
        uuid varchar(255),
        primary key (id)
    );

    create table faidspertrip (
        id bigint not null,
        trip_id_scheme_id_fatype_report_type varchar(255),
        primary key (id)
    );

    create table interval (
        interval_id bigint not null,
        interval_end timestamp,
        interval_start timestamp,
        interval_rule_id bigint,
        primary key (interval_id)
    );

    create table messageid (
        id bigint not null,
        message_id varchar(255) not null,
        validation_message_id bigint,
        primary key (id)
    );

    create table previousreport (
        prevrep_id bigint not null,
        prevrep_assetguid varchar(255),
        prevrep_positiontime timestamp,
        prevrep_updattim timestamp,
        prevrep_upuser varchar(255),
        primary key (prevrep_id)
    );

    create table rawassetidlist (
        rawassetidlist_id bigint not null,
        rawassetidlist_type varchar(255),
        rawassetidlist_value varchar(255),
        rawassetidlist_rawmoveasset_id bigint,
        primary key (rawassetidlist_id)
    );

    create table rawmessage (
        id bigint not null,
        raw_message_guid varchar(255),
        mdc_request_id varchar(255),
        raw_message text not null,
        raw_msg_type varchar(255),
        primary key (id)
    );

    create table rawmoveactivity (
        rawmoveact_id bigint not null,
        rawmoveact_callback varchar(255),
        rawmoveact_messageid varchar(255),
        rawmoveact_messagetype varchar(255),
        rawmoveact_rawmove_id bigint,
        primary key (rawmoveact_id)
    );

    create table rawmoveasset (
        rawmoveasset_id bigint not null,
        rawmoveasset_assettype varchar(255),
        rawmoveasset_rawmove_id bigint,
        primary key (rawmoveasset_id)
    );

    create table rawmovement (
        rawmove_id bigint not null,
        rawmove_active boolean,
        rawmove_assetname varchar(255),
        rawmove_comchanneltype varchar(255),
        rawmove_connectid varchar(255),
        rawmove_externalmarking varchar(255),
        rawmove_flagstate varchar(255),
        rawmove_guid varchar(255),
        rawmove_movementtype varchar(255),
        rawmove_positiontime timestamp,
        rawmove_reportedcourse double,
        rawmove_reportedspeed double,
        rawmove_source varchar(255),
        rawmove_status varchar(255),
        rawmove_updattim timestamp,
        rawmove_upuser varchar(255),
        rawmove_alarmrep_id bigint,
        primary key (rawmove_id)
    );

    create table rawmovemobileterminal (
        rawmovemob_id bigint not null,
        rawmovemob_connectid varchar(255),
        rawmovemob_guid varchar(255),
        rawmovemob_rawmove_id bigint,
        primary key (rawmovemob_id)
    );

    create table rawmovemobileterminalid (
        rawmovemobid_id bigint not null,
        rawmovemobid_type varchar(255),
        rawmovemobid_value varchar(255),
        rawmovemobid_rawmovemob_id bigint,
        primary key (rawmovemobid_id)
    );

    create table rawmoveposition (
        rawmovepos_id bigint not null,
        rawmovepos_altitude double,
        rawmovepos_latitude double,
        rawmovepos_longitude double,
        rawmovepos_rawmove_id bigint,
        primary key (rawmovepos_id)
    );

    create table rule (
        rule_id bigint not null,
        rule_created_on timestamp,
        br_id varchar(255) not null,
        disabled boolean,
        error_type varchar(255) not null,
        level varchar(255) not null,
        note text not null,
        property_names varchar(255) not null,
        template_id bigint,
        primary key (rule_id)
    );

    create table rulesegment (
        ruleseg_id bigint not null,
        ruleseg_condition varchar(255),
        ruleseg_criteria varchar(255),
        ruleseg_end_operator varchar(255),
        ruleseg_logic_operator varchar(255),
        ruleseg_order integer,
        ruleseg_start_operator varchar(255),
        ruleseg_subcriteria varchar(255),
        ruleseg_value varchar(255),
        ruleseg_rule_id bigint,
        primary key (ruleseg_id)
    );

    create table rulesubscription (
        rulesub_id bigint not null,
        rulesub_owner varchar(255),
        rulesub_type varchar(255),
        rulesub_rule_id bigint,
        primary key (rulesub_id)
    );

    create table sanityrule (
        sanityrule_id bigint not null,
        sanityrule_description varchar(255),
        sanityrule_expression varchar(255),
        sanityrule_guid varchar(255),
        sanityrule_name varchar(255),
        sanityrule_updattim timestamp,
        sanityrule_upuser varchar(255),
        primary key (sanityrule_id)
    );

    create table template (
        template_id bigint not null,
        template_name varchar(255) not null,
        fact_template varchar(255) not null,
        primary key (template_id)
    );

    create table ticket (
        ticket_id bigint not null,
        ticket_assetguid varchar(255),
        ticket_channelguid varchar(255),
        ticket_createddate timestamp,
        ticket_guid varchar(255),
        ticket_mobileterminalguid varchar(255),
        ticket_movementguid varchar(255),
        ticket_recipient varchar(255),
        ticket_ruleguid varchar(255),
        ticket_rulename varchar(255),
        ticket_status varchar(255),
        ticket_count bigint,
        ticket_updattim timestamp,
        ticket_upuser varchar(255),
        primary key (ticket_id)
    );

    create table validationmessage (
        id bigint not null,
        br_id varchar(255) not null,
        error_type varchar(255) not null,
        fact_date timestamp,
        level varchar(255) not null,
        message text not null,
        xpath_list text,
        raw_message_id bigint,
        primary key (id)
    );

    alter table context_expression 
        add constraint UKkbep01d90m2ng1iwkwmcmwyso unique (context, expression);

    alter table previousreport 
        add constraint UK_g124t9kxlb65nfed1my02ghyl unique (prevrep_assetguid);

    alter table rule 
        add constraint UK_exbochm35ucypqhkac4jj9utg unique (br_id);

    alter table template 
        add constraint UK_84ekg6bq3u5gxugfdreqw37np unique (template_name);

    alter table template 
        add constraint UK_ftdcn5j0igpppmkw6e6hb52tu unique (fact_template);

    alter table action 
        add constraint FKb7oseuh5vinsq5ulecytup949 
        foreign key (action_rule_id) 
        references customrule;

    alter table alarmitem 
        add constraint FKjja39hvyf0733dr3stjemn80x 
        foreign key (alarmitem_alarmrep_id) 
        references alarmreport;

    alter table context_expression 
        add constraint FK8vs5y07j703mcempqhodg1fmr 
        foreign key (rule_id) 
        references rule;

    alter table interval 
        add constraint FKbcyqfxiwuk9bq0hd5pa13evwr 
        foreign key (interval_rule_id) 
        references customrule;

    alter table messageid 
        add constraint FKt7r2o8i4r93s7bpobpipcfqv3 
        foreign key (validation_message_id) 
        references validationmessage;

    alter table rawassetidlist 
        add constraint FK7txlw62cqqgor87xew0gwk8va 
        foreign key (rawassetidlist_rawmoveasset_id) 
        references rawmoveasset;

    alter table rawmoveactivity 
        add constraint FKfd8vc1gf4feoemd0ags5x5ioa 
        foreign key (rawmoveact_rawmove_id) 
        references rawmovement;

    alter table rawmoveasset 
        add constraint FKij90sswfpojknp2qklwxnolrm 
        foreign key (rawmoveasset_rawmove_id) 
        references rawmovement;

    alter table rawmovement 
        add constraint FKgjvf4efqefliktu7av7ly3v0u 
        foreign key (rawmove_alarmrep_id) 
        references alarmreport;

    alter table rawmovemobileterminal 
        add constraint FKnmgu3bcb8gu8b0n0s9bm52rpx 
        foreign key (rawmovemob_rawmove_id) 
        references rawmovement;

    alter table rawmovemobileterminalid 
        add constraint FK9573ufagdntakncupdyv428dn 
        foreign key (rawmovemobid_rawmovemob_id) 
        references rawmovemobileterminal;

    alter table rawmoveposition 
        add constraint FK90egqhcqfcuu31frxw9x05502 
        foreign key (rawmovepos_rawmove_id) 
        references rawmovement;

    alter table rule 
        add constraint FKmx113051ieint865l7rs8g5ao 
        foreign key (template_id) 
        references template;

    alter table rulesegment 
        add constraint FKfak4712bpag1rwgh25t3ymbb3 
        foreign key (ruleseg_rule_id) 
        references customrule;

    alter table rulesubscription 
        add constraint FKf3hwdvcy8333lunb43tvngi52 
        foreign key (rulesub_rule_id) 
        references customrule;

    alter table validationmessage 
        add constraint FK9kyla6nb7ejou8gisc1hkem8q 
        foreign key (raw_message_id) 
        references rawmessage;
create sequence hibernate_sequence start with 1 increment by 1;

    create table action (
        action_id bigint not null,
        action_action varchar(255),
        action_order integer,
        action_value varchar(255),
        action_rule_id bigint,
        primary key (action_id)
    );

    create table alarmitem (
        alarmitem_id bigint not null,
        alarmitem_guid varchar(255),
        alarmitem_ruleguid varchar(255),
        alarmitem_rulename varchar(255),
        alarmitem_updattim timestamp,
        alarmitem_upuser varchar(255),
        alarmitem_alarmrep_id bigint,
        primary key (alarmitem_id)
    );

    create table alarmreport (
        alarmrep_id bigint not null,
        alarmrep_assetguid varchar(255),
        alarmrep_createddate timestamp,
        alarmrep_guid varchar(255),
        alarmrep_plugintype varchar(255),
        alarmrep_recipient varchar(255),
        alarmrep_status varchar(255),
        alarmrep_updattim timestamp,
        alarmrep_upuser varchar(255),
        primary key (alarmrep_id)
    );

    create table context_expression (
        id bigint not null,
        context varchar(255),
        expression varchar(255) not null,
        failure_message varchar(255),
        rule_id bigint,
        primary key (id)
    );

    create table customrule (
        rule_id bigint not null,
        rule_active boolean,
        rule_archived boolean,
        rule_availability varchar(255),
        rule_description varchar(255),
        rule_enddate timestamp,
        rule_guid varchar(255),
        rule_name varchar(255),
        rule_organisation varchar(255),
        rule_startdate timestamp,
        rule_lasttriggered timestamp,
        rule_updattim timestamp,
        rule_upuser varchar(255),
        primary key (rule_id)
    );

    create table FADocumentID (
        id bigint not null,
        created_on timestamp not null,
        type varchar(255),
        uuid varchar(255),
        primary key (id)
    );

    create table faidspertrip (
        id bigint not null,
        trip_id_scheme_id_fatype_report_type varchar(255),
        primary key (id)
    );

    create table interval (
        interval_id bigint not null,
        interval_end timestamp,
        interval_start timestamp,
        interval_rule_id bigint,
        primary key (interval_id)
    );

    create table messageid (
        id bigint not null,
        message_id varchar(255) not null,
        validation_message_id bigint,
        primary key (id)
    );

    create table previousreport (
        prevrep_id bigint not null,
        prevrep_assetguid varchar(255),
        prevrep_positiontime timestamp,
        prevrep_updattim timestamp,
        prevrep_upuser varchar(255),
        primary key (prevrep_id)
    );

    create table rawassetidlist (
        rawassetidlist_id bigint not null,
        rawassetidlist_type varchar(255),
        rawassetidlist_value varchar(255),
        rawassetidlist_rawmoveasset_id bigint,
        primary key (rawassetidlist_id)
    );

    create table rawmessage (
        id bigint not null,
        raw_message_guid varchar(255),
        mdc_request_id varchar(255),
        raw_message text not null,
        raw_msg_type varchar(255),
        primary key (id)
    );

    create table rawmoveactivity (
        rawmoveact_id bigint not null,
        rawmoveact_callback varchar(255),
        rawmoveact_messageid varchar(255),
        rawmoveact_messagetype varchar(255),
        rawmoveact_rawmove_id bigint,
        primary key (rawmoveact_id)
    );

    create table rawmoveasset (
        rawmoveasset_id bigint not null,
        rawmoveasset_assettype varchar(255),
        rawmoveasset_rawmove_id bigint,
        primary key (rawmoveasset_id)
    );

    create table rawmovement (
        rawmove_id bigint not null,
        rawmove_active boolean,
        rawmove_assetname varchar(255),
        rawmove_comchanneltype varchar(255),
        rawmove_connectid varchar(255),
        rawmove_externalmarking varchar(255),
        rawmove_flagstate varchar(255),
        rawmove_guid varchar(255),
        rawmove_movementtype varchar(255),
        rawmove_positiontime timestamp,
        rawmove_reportedcourse double,
        rawmove_reportedspeed double,
        rawmove_source varchar(255),
        rawmove_status varchar(255),
        rawmove_updattim timestamp,
        rawmove_upuser varchar(255),
        rawmove_alarmrep_id bigint,
        primary key (rawmove_id)
    );

    create table rawmovemobileterminal (
        rawmovemob_id bigint not null,
        rawmovemob_connectid varchar(255),
        rawmovemob_guid varchar(255),
        rawmovemob_rawmove_id bigint,
        primary key (rawmovemob_id)
    );

    create table rawmovemobileterminalid (
        rawmovemobid_id bigint not null,
        rawmovemobid_type varchar(255),
        rawmovemobid_value varchar(255),
        rawmovemobid_rawmovemob_id bigint,
        primary key (rawmovemobid_id)
    );

    create table rawmoveposition (
        rawmovepos_id bigint not null,
        rawmovepos_altitude double,
        rawmovepos_latitude double,
        rawmovepos_longitude double,
        rawmovepos_rawmove_id bigint,
        primary key (rawmovepos_id)
    );

    create table rule (
        rule_id bigint not null,
        rule_created_on timestamp,
        br_id varchar(255) not null,
        disabled boolean,
        error_type varchar(255) not null,
        level varchar(255) not null,
        note text not null,
        property_names varchar(255) not null,
        template_id bigint,
        primary key (rule_id)
    );

    create table rulesegment (
        ruleseg_id bigint not null,
        ruleseg_condition varchar(255),
        ruleseg_criteria varchar(255),
        ruleseg_end_operator varchar(255),
        ruleseg_logic_operator varchar(255),
        ruleseg_order integer,
        ruleseg_start_operator varchar(255),
        ruleseg_subcriteria varchar(255),
        ruleseg_value varchar(255),
        ruleseg_rule_id bigint,
        primary key (ruleseg_id)
    );

    create table rulesubscription (
        rulesub_id bigint not null,
        rulesub_owner varchar(255),
        rulesub_type varchar(255),
        rulesub_rule_id bigint,
        primary key (rulesub_id)
    );

    create table sanityrule (
        sanityrule_id bigint not null,
        sanityrule_description varchar(255),
        sanityrule_expression varchar(255),
        sanityrule_guid varchar(255),
        sanityrule_name varchar(255),
        sanityrule_updattim timestamp,
        sanityrule_upuser varchar(255),
        primary key (sanityrule_id)
    );

    create table template (
        template_id bigint not null,
        template_name varchar(255) not null,
        fact_template varchar(255) not null,
        primary key (template_id)
    );

    create table ticket (
        ticket_id bigint not null,
        ticket_assetguid varchar(255),
        ticket_channelguid varchar(255),
        ticket_createddate timestamp,
        ticket_guid varchar(255),
        ticket_mobileterminalguid varchar(255),
        ticket_movementguid varchar(255),
        ticket_recipient varchar(255),
        ticket_ruleguid varchar(255),
        ticket_rulename varchar(255),
        ticket_status varchar(255),
        ticket_count bigint,
        ticket_updattim timestamp,
        ticket_upuser varchar(255),
        primary key (ticket_id)
    );

    create table validationmessage (
        id bigint not null,
        br_id varchar(255) not null,
        error_type varchar(255) not null,
        fact_date timestamp,
        level varchar(255) not null,
        message text not null,
        xpath_list text,
        raw_message_id bigint,
        primary key (id)
    );

    alter table context_expression 
        add constraint UKkbep01d90m2ng1iwkwmcmwyso unique (context, expression);

    alter table previousreport 
        add constraint UK_g124t9kxlb65nfed1my02ghyl unique (prevrep_assetguid);

    alter table rule 
        add constraint UK_exbochm35ucypqhkac4jj9utg unique (br_id);

    alter table template 
        add constraint UK_84ekg6bq3u5gxugfdreqw37np unique (template_name);

    alter table template 
        add constraint UK_ftdcn5j0igpppmkw6e6hb52tu unique (fact_template);

    alter table action 
        add constraint FKb7oseuh5vinsq5ulecytup949 
        foreign key (action_rule_id) 
        references customrule;

    alter table alarmitem 
        add constraint FKjja39hvyf0733dr3stjemn80x 
        foreign key (alarmitem_alarmrep_id) 
        references alarmreport;

    alter table context_expression 
        add constraint FK8vs5y07j703mcempqhodg1fmr 
        foreign key (rule_id) 
        references rule;

    alter table interval 
        add constraint FKbcyqfxiwuk9bq0hd5pa13evwr 
        foreign key (interval_rule_id) 
        references customrule;

    alter table messageid 
        add constraint FKt7r2o8i4r93s7bpobpipcfqv3 
        foreign key (validation_message_id) 
        references validationmessage;

    alter table rawassetidlist 
        add constraint FK7txlw62cqqgor87xew0gwk8va 
        foreign key (rawassetidlist_rawmoveasset_id) 
        references rawmoveasset;

    alter table rawmoveactivity 
        add constraint FKfd8vc1gf4feoemd0ags5x5ioa 
        foreign key (rawmoveact_rawmove_id) 
        references rawmovement;

    alter table rawmoveasset 
        add constraint FKij90sswfpojknp2qklwxnolrm 
        foreign key (rawmoveasset_rawmove_id) 
        references rawmovement;

    alter table rawmovement 
        add constraint FKgjvf4efqefliktu7av7ly3v0u 
        foreign key (rawmove_alarmrep_id) 
        references alarmreport;

    alter table rawmovemobileterminal 
        add constraint FKnmgu3bcb8gu8b0n0s9bm52rpx 
        foreign key (rawmovemob_rawmove_id) 
        references rawmovement;

    alter table rawmovemobileterminalid 
        add constraint FK9573ufagdntakncupdyv428dn 
        foreign key (rawmovemobid_rawmovemob_id) 
        references rawmovemobileterminal;

    alter table rawmoveposition 
        add constraint FK90egqhcqfcuu31frxw9x05502 
        foreign key (rawmovepos_rawmove_id) 
        references rawmovement;

    alter table rule 
        add constraint FKmx113051ieint865l7rs8g5ao 
        foreign key (template_id) 
        references template;

    alter table rulesegment 
        add constraint FKfak4712bpag1rwgh25t3ymbb3 
        foreign key (ruleseg_rule_id) 
        references customrule;

    alter table rulesubscription 
        add constraint FKf3hwdvcy8333lunb43tvngi52 
        foreign key (rulesub_rule_id) 
        references customrule;

    alter table validationmessage 
        add constraint FK9kyla6nb7ejou8gisc1hkem8q 
        foreign key (raw_message_id) 
        references rawmessage;
create sequence hibernate_sequence start with 1 increment by 1;

    create table action (
        action_id bigint not null,
        action_action varchar(255),
        action_order integer,
        action_value varchar(255),
        action_rule_id bigint,
        primary key (action_id)
    );

    create table alarmitem (
        alarmitem_id bigint not null,
        alarmitem_guid varchar(255),
        alarmitem_ruleguid varchar(255),
        alarmitem_rulename varchar(255),
        alarmitem_updattim timestamp,
        alarmitem_upuser varchar(255),
        alarmitem_alarmrep_id bigint,
        primary key (alarmitem_id)
    );

    create table alarmreport (
        alarmrep_id bigint not null,
        alarmrep_assetguid varchar(255),
        alarmrep_createddate timestamp,
        alarmrep_guid varchar(255),
        alarmrep_plugintype varchar(255),
        alarmrep_recipient varchar(255),
        alarmrep_status varchar(255),
        alarmrep_updattim timestamp,
        alarmrep_upuser varchar(255),
        primary key (alarmrep_id)
    );

    create table context_expression (
        id bigint not null,
        context varchar(255),
        expression varchar(255) not null,
        failure_message varchar(255),
        rule_id bigint,
        primary key (id)
    );

    create table customrule (
        rule_id bigint not null,
        rule_active boolean,
        rule_archived boolean,
        rule_availability varchar(255),
        rule_description varchar(255),
        rule_enddate timestamp,
        rule_guid varchar(255),
        rule_name varchar(255),
        rule_organisation varchar(255),
        rule_startdate timestamp,
        rule_lasttriggered timestamp,
        rule_updattim timestamp,
        rule_upuser varchar(255),
        primary key (rule_id)
    );

    create table FADocumentID (
        id bigint not null,
        created_on timestamp not null,
        type varchar(255),
        uuid varchar(255),
        primary key (id)
    );

    create table faidspertrip (
        id bigint not null,
        trip_id_scheme_id_fatype_report_type varchar(255),
        primary key (id)
    );

    create table interval (
        interval_id bigint not null,
        interval_end timestamp,
        interval_start timestamp,
        interval_rule_id bigint,
        primary key (interval_id)
    );

    create table messageid (
        id bigint not null,
        message_id varchar(255) not null,
        validation_message_id bigint,
        primary key (id)
    );

    create table previousreport (
        prevrep_id bigint not null,
        prevrep_assetguid varchar(255),
        prevrep_positiontime timestamp,
        prevrep_updattim timestamp,
        prevrep_upuser varchar(255),
        primary key (prevrep_id)
    );

    create table rawassetidlist (
        rawassetidlist_id bigint not null,
        rawassetidlist_type varchar(255),
        rawassetidlist_value varchar(255),
        rawassetidlist_rawmoveasset_id bigint,
        primary key (rawassetidlist_id)
    );

    create table rawmessage (
        id bigint not null,
        raw_message_guid varchar(255),
        mdc_request_id varchar(255),
        raw_message text not null,
        raw_msg_type varchar(255),
        primary key (id)
    );

    create table rawmoveactivity (
        rawmoveact_id bigint not null,
        rawmoveact_callback varchar(255),
        rawmoveact_messageid varchar(255),
        rawmoveact_messagetype varchar(255),
        rawmoveact_rawmove_id bigint,
        primary key (rawmoveact_id)
    );

    create table rawmoveasset (
        rawmoveasset_id bigint not null,
        rawmoveasset_assettype varchar(255),
        rawmoveasset_rawmove_id bigint,
        primary key (rawmoveasset_id)
    );

    create table rawmovement (
        rawmove_id bigint not null,
        rawmove_active boolean,
        rawmove_assetname varchar(255),
        rawmove_comchanneltype varchar(255),
        rawmove_connectid varchar(255),
        rawmove_externalmarking varchar(255),
        rawmove_flagstate varchar(255),
        rawmove_guid varchar(255),
        rawmove_movementtype varchar(255),
        rawmove_positiontime timestamp,
        rawmove_reportedcourse double,
        rawmove_reportedspeed double,
        rawmove_source varchar(255),
        rawmove_status varchar(255),
        rawmove_updattim timestamp,
        rawmove_upuser varchar(255),
        rawmove_alarmrep_id bigint,
        primary key (rawmove_id)
    );

    create table rawmovemobileterminal (
        rawmovemob_id bigint not null,
        rawmovemob_connectid varchar(255),
        rawmovemob_guid varchar(255),
        rawmovemob_rawmove_id bigint,
        primary key (rawmovemob_id)
    );

    create table rawmovemobileterminalid (
        rawmovemobid_id bigint not null,
        rawmovemobid_type varchar(255),
        rawmovemobid_value varchar(255),
        rawmovemobid_rawmovemob_id bigint,
        primary key (rawmovemobid_id)
    );

    create table rawmoveposition (
        rawmovepos_id bigint not null,
        rawmovepos_altitude double,
        rawmovepos_latitude double,
        rawmovepos_longitude double,
        rawmovepos_rawmove_id bigint,
        primary key (rawmovepos_id)
    );

    create table rule (
        rule_id bigint not null,
        rule_created_on timestamp,
        br_id varchar(255) not null,
        disabled boolean,
        error_type varchar(255) not null,
        level varchar(255) not null,
        note text not null,
        property_names varchar(255) not null,
        template_id bigint,
        primary key (rule_id)
    );

    create table rulesegment (
        ruleseg_id bigint not null,
        ruleseg_condition varchar(255),
        ruleseg_criteria varchar(255),
        ruleseg_end_operator varchar(255),
        ruleseg_logic_operator varchar(255),
        ruleseg_order integer,
        ruleseg_start_operator varchar(255),
        ruleseg_subcriteria varchar(255),
        ruleseg_value varchar(255),
        ruleseg_rule_id bigint,
        primary key (ruleseg_id)
    );

    create table rulesubscription (
        rulesub_id bigint not null,
        rulesub_owner varchar(255),
        rulesub_type varchar(255),
        rulesub_rule_id bigint,
        primary key (rulesub_id)
    );

    create table sanityrule (
        sanityrule_id bigint not null,
        sanityrule_description varchar(255),
        sanityrule_expression varchar(255),
        sanityrule_guid varchar(255),
        sanityrule_name varchar(255),
        sanityrule_updattim timestamp,
        sanityrule_upuser varchar(255),
        primary key (sanityrule_id)
    );

    create table template (
        template_id bigint not null,
        template_name varchar(255) not null,
        fact_template varchar(255) not null,
        primary key (template_id)
    );

    create table ticket (
        ticket_id bigint not null,
        ticket_assetguid varchar(255),
        ticket_channelguid varchar(255),
        ticket_createddate timestamp,
        ticket_guid varchar(255),
        ticket_mobileterminalguid varchar(255),
        ticket_movementguid varchar(255),
        ticket_recipient varchar(255),
        ticket_ruleguid varchar(255),
        ticket_rulename varchar(255),
        ticket_status varchar(255),
        ticket_count bigint,
        ticket_updattim timestamp,
        ticket_upuser varchar(255),
        primary key (ticket_id)
    );

    create table validationmessage (
        id bigint not null,
        br_id varchar(255) not null,
        error_type varchar(255) not null,
        fact_date timestamp,
        level varchar(255) not null,
        message text not null,
        xpath_list text,
        raw_message_id bigint,
        primary key (id)
    );

    alter table context_expression 
        add constraint UKkbep01d90m2ng1iwkwmcmwyso unique (context, expression);

    alter table previousreport 
        add constraint UK_g124t9kxlb65nfed1my02ghyl unique (prevrep_assetguid);

    alter table rule 
        add constraint UK_exbochm35ucypqhkac4jj9utg unique (br_id);

    alter table template 
        add constraint UK_84ekg6bq3u5gxugfdreqw37np unique (template_name);

    alter table template 
        add constraint UK_ftdcn5j0igpppmkw6e6hb52tu unique (fact_template);

    alter table action 
        add constraint FKb7oseuh5vinsq5ulecytup949 
        foreign key (action_rule_id) 
        references customrule;

    alter table alarmitem 
        add constraint FKjja39hvyf0733dr3stjemn80x 
        foreign key (alarmitem_alarmrep_id) 
        references alarmreport;

    alter table context_expression 
        add constraint FK8vs5y07j703mcempqhodg1fmr 
        foreign key (rule_id) 
        references rule;

    alter table interval 
        add constraint FKbcyqfxiwuk9bq0hd5pa13evwr 
        foreign key (interval_rule_id) 
        references customrule;

    alter table messageid 
        add constraint FKt7r2o8i4r93s7bpobpipcfqv3 
        foreign key (validation_message_id) 
        references validationmessage;

    alter table rawassetidlist 
        add constraint FK7txlw62cqqgor87xew0gwk8va 
        foreign key (rawassetidlist_rawmoveasset_id) 
        references rawmoveasset;

    alter table rawmoveactivity 
        add constraint FKfd8vc1gf4feoemd0ags5x5ioa 
        foreign key (rawmoveact_rawmove_id) 
        references rawmovement;

    alter table rawmoveasset 
        add constraint FKij90sswfpojknp2qklwxnolrm 
        foreign key (rawmoveasset_rawmove_id) 
        references rawmovement;

    alter table rawmovement 
        add constraint FKgjvf4efqefliktu7av7ly3v0u 
        foreign key (rawmove_alarmrep_id) 
        references alarmreport;

    alter table rawmovemobileterminal 
        add constraint FKnmgu3bcb8gu8b0n0s9bm52rpx 
        foreign key (rawmovemob_rawmove_id) 
        references rawmovement;

    alter table rawmovemobileterminalid 
        add constraint FK9573ufagdntakncupdyv428dn 
        foreign key (rawmovemobid_rawmovemob_id) 
        references rawmovemobileterminal;

    alter table rawmoveposition 
        add constraint FK90egqhcqfcuu31frxw9x05502 
        foreign key (rawmovepos_rawmove_id) 
        references rawmovement;

    alter table rule 
        add constraint FKmx113051ieint865l7rs8g5ao 
        foreign key (template_id) 
        references template;

    alter table rulesegment 
        add constraint FKfak4712bpag1rwgh25t3ymbb3 
        foreign key (ruleseg_rule_id) 
        references customrule;

    alter table rulesubscription 
        add constraint FKf3hwdvcy8333lunb43tvngi52 
        foreign key (rulesub_rule_id) 
        references customrule;

    alter table validationmessage 
        add constraint FK9kyla6nb7ejou8gisc1hkem8q 
        foreign key (raw_message_id) 
        references rawmessage;
create sequence hibernate_sequence start with 1 increment by 1;

    create table action (
        action_id bigint not null,
        action_action varchar(255),
        action_order integer,
        action_value varchar(255),
        action_rule_id bigint,
        primary key (action_id)
    );

    create table alarmitem (
        alarmitem_id bigint not null,
        alarmitem_guid varchar(255),
        alarmitem_ruleguid varchar(255),
        alarmitem_rulename varchar(255),
        alarmitem_updattim timestamp,
        alarmitem_upuser varchar(255),
        alarmitem_alarmrep_id bigint,
        primary key (alarmitem_id)
    );

    create table alarmreport (
        alarmrep_id bigint not null,
        alarmrep_assetguid varchar(255),
        alarmrep_createddate timestamp,
        alarmrep_guid varchar(255),
        alarmrep_plugintype varchar(255),
        alarmrep_recipient varchar(255),
        alarmrep_status varchar(255),
        alarmrep_updattim timestamp,
        alarmrep_upuser varchar(255),
        primary key (alarmrep_id)
    );

    create table context_expression (
        id bigint not null,
        context varchar(255),
        expression varchar(255) not null,
        failure_message varchar(255),
        rule_id bigint,
        primary key (id)
    );

    create table customrule (
        rule_id bigint not null,
        rule_active boolean,
        rule_archived boolean,
        rule_availability varchar(255),
        rule_description varchar(255),
        rule_enddate timestamp,
        rule_guid varchar(255),
        rule_name varchar(255),
        rule_organisation varchar(255),
        rule_startdate timestamp,
        rule_lasttriggered timestamp,
        rule_updattim timestamp,
        rule_upuser varchar(255),
        primary key (rule_id)
    );

    create table FADocumentID (
        id bigint not null,
        created_on timestamp not null,
        type varchar(255),
        uuid varchar(255),
        primary key (id)
    );

    create table faidspertrip (
        id bigint not null,
        trip_id_scheme_id_fatype_report_type varchar(255),
        primary key (id)
    );

    create table interval (
        interval_id bigint not null,
        interval_end timestamp,
        interval_start timestamp,
        interval_rule_id bigint,
        primary key (interval_id)
    );

    create table messageid (
        id bigint not null,
        message_id varchar(255) not null,
        validation_message_id bigint,
        primary key (id)
    );

    create table previousreport (
        prevrep_id bigint not null,
        prevrep_assetguid varchar(255),
        prevrep_positiontime timestamp,
        prevrep_updattim timestamp,
        prevrep_upuser varchar(255),
        primary key (prevrep_id)
    );

    create table rawassetidlist (
        rawassetidlist_id bigint not null,
        rawassetidlist_type varchar(255),
        rawassetidlist_value varchar(255),
        rawassetidlist_rawmoveasset_id bigint,
        primary key (rawassetidlist_id)
    );

    create table rawmessage (
        id bigint not null,
        raw_message_guid varchar(255),
        mdc_request_id varchar(255),
        raw_message text not null,
        raw_msg_type varchar(255),
        primary key (id)
    );

    create table rawmoveactivity (
        rawmoveact_id bigint not null,
        rawmoveact_callback varchar(255),
        rawmoveact_messageid varchar(255),
        rawmoveact_messagetype varchar(255),
        rawmoveact_rawmove_id bigint,
        primary key (rawmoveact_id)
    );

    create table rawmoveasset (
        rawmoveasset_id bigint not null,
        rawmoveasset_assettype varchar(255),
        rawmoveasset_rawmove_id bigint,
        primary key (rawmoveasset_id)
    );

    create table rawmovement (
        rawmove_id bigint not null,
        rawmove_active boolean,
        rawmove_assetname varchar(255),
        rawmove_comchanneltype varchar(255),
        rawmove_connectid varchar(255),
        rawmove_externalmarking varchar(255),
        rawmove_flagstate varchar(255),
        rawmove_guid varchar(255),
        rawmove_movementtype varchar(255),
        rawmove_positiontime timestamp,
        rawmove_reportedcourse double,
        rawmove_reportedspeed double,
        rawmove_source varchar(255),
        rawmove_status varchar(255),
        rawmove_updattim timestamp,
        rawmove_upuser varchar(255),
        rawmove_alarmrep_id bigint,
        primary key (rawmove_id)
    );

    create table rawmovemobileterminal (
        rawmovemob_id bigint not null,
        rawmovemob_connectid varchar(255),
        rawmovemob_guid varchar(255),
        rawmovemob_rawmove_id bigint,
        primary key (rawmovemob_id)
    );

    create table rawmovemobileterminalid (
        rawmovemobid_id bigint not null,
        rawmovemobid_type varchar(255),
        rawmovemobid_value varchar(255),
        rawmovemobid_rawmovemob_id bigint,
        primary key (rawmovemobid_id)
    );

    create table rawmoveposition (
        rawmovepos_id bigint not null,
        rawmovepos_altitude double,
        rawmovepos_latitude double,
        rawmovepos_longitude double,
        rawmovepos_rawmove_id bigint,
        primary key (rawmovepos_id)
    );

    create table rule (
        rule_id bigint not null,
        rule_created_on timestamp,
        br_id varchar(255) not null,
        disabled boolean,
        error_type varchar(255) not null,
        level varchar(255) not null,
        note text not null,
        property_names varchar(255) not null,
        template_id bigint,
        primary key (rule_id)
    );

    create table rulesegment (
        ruleseg_id bigint not null,
        ruleseg_condition varchar(255),
        ruleseg_criteria varchar(255),
        ruleseg_end_operator varchar(255),
        ruleseg_logic_operator varchar(255),
        ruleseg_order integer,
        ruleseg_start_operator varchar(255),
        ruleseg_subcriteria varchar(255),
        ruleseg_value varchar(255),
        ruleseg_rule_id bigint,
        primary key (ruleseg_id)
    );

    create table rulesubscription (
        rulesub_id bigint not null,
        rulesub_owner varchar(255),
        rulesub_type varchar(255),
        rulesub_rule_id bigint,
        primary key (rulesub_id)
    );

    create table sanityrule (
        sanityrule_id bigint not null,
        sanityrule_description varchar(255),
        sanityrule_expression varchar(255),
        sanityrule_guid varchar(255),
        sanityrule_name varchar(255),
        sanityrule_updattim timestamp,
        sanityrule_upuser varchar(255),
        primary key (sanityrule_id)
    );

    create table template (
        template_id bigint not null,
        template_name varchar(255) not null,
        fact_template varchar(255) not null,
        primary key (template_id)
    );

    create table ticket (
        ticket_id bigint not null,
        ticket_assetguid varchar(255),
        ticket_channelguid varchar(255),
        ticket_createddate timestamp,
        ticket_guid varchar(255),
        ticket_mobileterminalguid varchar(255),
        ticket_movementguid varchar(255),
        ticket_recipient varchar(255),
        ticket_ruleguid varchar(255),
        ticket_rulename varchar(255),
        ticket_status varchar(255),
        ticket_count bigint,
        ticket_updattim timestamp,
        ticket_upuser varchar(255),
        primary key (ticket_id)
    );

    create table validationmessage (
        id bigint not null,
        br_id varchar(255) not null,
        error_type varchar(255) not null,
        fact_date timestamp,
        level varchar(255) not null,
        message text not null,
        xpath_list text,
        raw_message_id bigint,
        primary key (id)
    );

    alter table context_expression 
        add constraint UKkbep01d90m2ng1iwkwmcmwyso unique (context, expression);

    alter table previousreport 
        add constraint UK_g124t9kxlb65nfed1my02ghyl unique (prevrep_assetguid);

    alter table rule 
        add constraint UK_exbochm35ucypqhkac4jj9utg unique (br_id);

    alter table template 
        add constraint UK_84ekg6bq3u5gxugfdreqw37np unique (template_name);

    alter table template 
        add constraint UK_ftdcn5j0igpppmkw6e6hb52tu unique (fact_template);

    alter table action 
        add constraint FKb7oseuh5vinsq5ulecytup949 
        foreign key (action_rule_id) 
        references customrule;

    alter table alarmitem 
        add constraint FKjja39hvyf0733dr3stjemn80x 
        foreign key (alarmitem_alarmrep_id) 
        references alarmreport;

    alter table context_expression 
        add constraint FK8vs5y07j703mcempqhodg1fmr 
        foreign key (rule_id) 
        references rule;

    alter table interval 
        add constraint FKbcyqfxiwuk9bq0hd5pa13evwr 
        foreign key (interval_rule_id) 
        references customrule;

    alter table messageid 
        add constraint FKt7r2o8i4r93s7bpobpipcfqv3 
        foreign key (validation_message_id) 
        references validationmessage;

    alter table rawassetidlist 
        add constraint FK7txlw62cqqgor87xew0gwk8va 
        foreign key (rawassetidlist_rawmoveasset_id) 
        references rawmoveasset;

    alter table rawmoveactivity 
        add constraint FKfd8vc1gf4feoemd0ags5x5ioa 
        foreign key (rawmoveact_rawmove_id) 
        references rawmovement;

    alter table rawmoveasset 
        add constraint FKij90sswfpojknp2qklwxnolrm 
        foreign key (rawmoveasset_rawmove_id) 
        references rawmovement;

    alter table rawmovement 
        add constraint FKgjvf4efqefliktu7av7ly3v0u 
        foreign key (rawmove_alarmrep_id) 
        references alarmreport;

    alter table rawmovemobileterminal 
        add constraint FKnmgu3bcb8gu8b0n0s9bm52rpx 
        foreign key (rawmovemob_rawmove_id) 
        references rawmovement;

    alter table rawmovemobileterminalid 
        add constraint FK9573ufagdntakncupdyv428dn 
        foreign key (rawmovemobid_rawmovemob_id) 
        references rawmovemobileterminal;

    alter table rawmoveposition 
        add constraint FK90egqhcqfcuu31frxw9x05502 
        foreign key (rawmovepos_rawmove_id) 
        references rawmovement;

    alter table rule 
        add constraint FKmx113051ieint865l7rs8g5ao 
        foreign key (template_id) 
        references template;

    alter table rulesegment 
        add constraint FKfak4712bpag1rwgh25t3ymbb3 
        foreign key (ruleseg_rule_id) 
        references customrule;

    alter table rulesubscription 
        add constraint FKf3hwdvcy8333lunb43tvngi52 
        foreign key (rulesub_rule_id) 
        references customrule;

    alter table validationmessage 
        add constraint FK9kyla6nb7ejou8gisc1hkem8q 
        foreign key (raw_message_id) 
        references rawmessage;
