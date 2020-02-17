create table rules.messageid (id int8 not null, message_id varchar(255) not null, validation_message_id int8, primary key (id))
create table rules.rawmessage (id int8 not null, raw_message_guid varchar(255), mdc_request_id varchar(255), raw_message text not null, raw_msg_type varchar(255), primary key (id))
create table rules.rule (rule_id int8 not null, rule_created_on timestamp, br_id varchar(255) not null, disabled boolean, error_type varchar(255) not null, expression text not null, level varchar(255) not null, message text not null, note text not null, property_names varchar(255) not null, template_id int8, primary key (rule_id))
create table rules.template (template_id int8 not null, template_name varchar(255) not null, fact_template varchar(255) not null, primary key (template_id))
create table rules.validationmessage (id int8 not null, br_id varchar(255) not null, error_type varchar(255) not null, level varchar(255) not null, message text not null, xpath_list text, raw_message_id int8, primary key (id))
alter table rules.rule add constraint UK_exbochm35ucypqhkac4jj9utg unique (br_id)
alter table rules.template add constraint UK_84ekg6bq3u5gxugfdreqw37np unique (template_name)
alter table rules.template add constraint UK_ftdcn5j0igpppmkw6e6hb52tu unique (fact_template)
alter table rules.messageid add constraint FK_sfsxbqls9pm1i30d34n51nndg foreign key (validation_message_id) references rules.validationmessage
alter table rules.rule add constraint FK_cqshh8msujtb44r02dbxm4edt foreign key (template_id) references rules.template
alter table rules.validationmessage add constraint FK_iluwhraqjw4vxtq1jgte44a8p foreign key (raw_message_id) references rules.rawmessage
create sequence rules.hibernate_sequence
create sequence rules.hibernate_sequence start 1 increment 1
create table rules.context_expression (id int8 not null, context varchar(255), expression varchar(255) not null, failure_message varchar(255), rule_id int8, primary key (id))
create table rules.messageid (id int8 not null, message_id varchar(255) not null, validation_message_id int8, primary key (id))
create table rules.rawmessage (id int8 not null, raw_message_guid varchar(255), mdc_request_id varchar(255), raw_message text not null, raw_msg_type varchar(255), primary key (id))
create table rules.rule (rule_id int8 not null, rule_created_on timestamp, br_id varchar(255) not null, disabled boolean, error_type varchar(255) not null, level varchar(255) not null, note text not null, property_names varchar(255) not null, template_id int8, primary key (rule_id))
create table rules.template (template_id int8 not null, template_name varchar(255) not null, fact_template varchar(255) not null, primary key (template_id))
create table rules.validationmessage (id int8 not null, br_id varchar(255) not null, error_type varchar(255) not null, fact_date timestamp, level varchar(255) not null, message text not null, xpath_list text, raw_message_id int8, primary key (id))
alter table rules.context_expression add constraint UKkbep01d90m2ng1iwkwmcmwyso unique (context, expression)
alter table rules.rule add constraint UK_exbochm35ucypqhkac4jj9utg unique (br_id)
alter table rules.template add constraint UK_84ekg6bq3u5gxugfdreqw37np unique (template_name)
alter table rules.template add constraint UK_ftdcn5j0igpppmkw6e6hb52tu unique (fact_template)
alter table rules.context_expression add constraint FK8vs5y07j703mcempqhodg1fmr foreign key (rule_id) references rules.rule
alter table rules.messageid add constraint FKt7r2o8i4r93s7bpobpipcfqv3 foreign key (validation_message_id) references rules.validationmessage
alter table rules.rule add constraint FKmx113051ieint865l7rs8g5ao foreign key (template_id) references rules.template
alter table rules.validationmessage add constraint FK9kyla6nb7ejou8gisc1hkem8q foreign key (raw_message_id) references rules.rawmessage
create sequence rules.hibernate_sequence start 1 increment 1
create table rules.context_expression (id int8 not null, context varchar(255), expression varchar(255) not null, failure_message varchar(255), rule_id int8, primary key (id))
create table rules.messageid (id int8 not null, message_id varchar(255) not null, validation_message_id int8, primary key (id))
create table rules.rawmessage (id int8 not null, raw_message_guid varchar(255), mdc_request_id varchar(255), raw_message text not null, raw_msg_type varchar(255), primary key (id))
create table rules.rule (rule_id int8 not null, rule_created_on timestamp, br_id varchar(255) not null, disabled boolean, error_type varchar(255) not null, level varchar(255) not null, note text not null, property_names varchar(255) not null, template_id int8, primary key (rule_id))
create table rules.template (template_id int8 not null, template_name varchar(255) not null, fact_template varchar(255) not null, primary key (template_id))
create table rules.validationmessage (id int8 not null, br_id varchar(255) not null, error_type varchar(255) not null, fact_date timestamp, level varchar(255) not null, message text not null, xpath_list text, raw_message_id int8, primary key (id))
alter table rules.context_expression add constraint UKkbep01d90m2ng1iwkwmcmwyso unique (context, expression)
alter table rules.rule add constraint UK_exbochm35ucypqhkac4jj9utg unique (br_id)
alter table rules.template add constraint UK_84ekg6bq3u5gxugfdreqw37np unique (template_name)
alter table rules.template add constraint UK_ftdcn5j0igpppmkw6e6hb52tu unique (fact_template)
alter table rules.context_expression add constraint FK8vs5y07j703mcempqhodg1fmr foreign key (rule_id) references rules.rule
alter table rules.messageid add constraint FKt7r2o8i4r93s7bpobpipcfqv3 foreign key (validation_message_id) references rules.validationmessage
alter table rules.rule add constraint FKmx113051ieint865l7rs8g5ao foreign key (template_id) references rules.template
alter table rules.validationmessage add constraint FK9kyla6nb7ejou8gisc1hkem8q foreign key (raw_message_id) references rules.rawmessage
create sequence rules.hibernate_sequence start 1 increment 1
create table rules.context_expression (id int8 not null, context varchar(255), expression varchar(255) not null, failure_message varchar(255), rule_id int8, primary key (id))
create table rules.messageid (id int8 not null, message_id varchar(255) not null, validation_message_id int8, primary key (id))
create table rules.rawmessage (id int8 not null, raw_message_guid varchar(255), mdc_request_id varchar(255), raw_message text not null, raw_msg_type varchar(255), primary key (id))
create table rules.rule (rule_id int8 not null, rule_created_on timestamp, br_id varchar(255) not null, disabled boolean, error_type varchar(255) not null, level varchar(255) not null, note text not null, property_names varchar(255) not null, template_id int8, primary key (rule_id))
create table rules.template (template_id int8 not null, template_name varchar(255) not null, fact_template varchar(255) not null, primary key (template_id))
create table rules.validationmessage (id int8 not null, br_id varchar(255) not null, error_type varchar(255) not null, fact_date timestamp, level varchar(255) not null, message text not null, xpath_list text, raw_message_id int8, primary key (id))
alter table rules.context_expression add constraint UKkbep01d90m2ng1iwkwmcmwyso unique (context, expression)
alter table rules.rule add constraint UK_exbochm35ucypqhkac4jj9utg unique (br_id)
alter table rules.template add constraint UK_84ekg6bq3u5gxugfdreqw37np unique (template_name)
alter table rules.template add constraint UK_ftdcn5j0igpppmkw6e6hb52tu unique (fact_template)
alter table rules.context_expression add constraint FK8vs5y07j703mcempqhodg1fmr foreign key (rule_id) references rules.rule
alter table rules.messageid add constraint FKt7r2o8i4r93s7bpobpipcfqv3 foreign key (validation_message_id) references rules.validationmessage
alter table rules.rule add constraint FKmx113051ieint865l7rs8g5ao foreign key (template_id) references rules.template
alter table rules.validationmessage add constraint FK9kyla6nb7ejou8gisc1hkem8q foreign key (raw_message_id) references rules.rawmessage
create sequence rules.hibernate_sequence start 1 increment 1
create table rules.context_expression (id int8 not null, context varchar(255), expression varchar(255) not null, failure_message varchar(255), rule_id int8, primary key (id))
create table rules.messageid (id int8 not null, message_id varchar(255) not null, validation_message_id int8, primary key (id))
create table rules.rawmessage (id int8 not null, raw_message_guid varchar(255), mdc_request_id varchar(255), raw_message text not null, raw_msg_type varchar(255), primary key (id))
create table rules.rule (rule_id int8 not null, rule_created_on timestamp, br_id varchar(255) not null, disabled boolean, error_type varchar(255) not null, level varchar(255) not null, note text not null, property_names varchar(255) not null, template_id int8, primary key (rule_id))
create table rules.template (template_id int8 not null, template_name varchar(255) not null, fact_template varchar(255) not null, primary key (template_id))
create table rules.validationmessage (id int8 not null, br_id varchar(255) not null, error_type varchar(255) not null, fact_date timestamp, level varchar(255) not null, message text not null, xpath_list text, raw_message_id int8, primary key (id))
alter table rules.context_expression add constraint UKkbep01d90m2ng1iwkwmcmwyso unique (context, expression)
alter table rules.rule add constraint UK_exbochm35ucypqhkac4jj9utg unique (br_id)
alter table rules.template add constraint UK_84ekg6bq3u5gxugfdreqw37np unique (template_name)
alter table rules.template add constraint UK_ftdcn5j0igpppmkw6e6hb52tu unique (fact_template)
alter table rules.context_expression add constraint FK8vs5y07j703mcempqhodg1fmr foreign key (rule_id) references rules.rule
alter table rules.messageid add constraint FKt7r2o8i4r93s7bpobpipcfqv3 foreign key (validation_message_id) references rules.validationmessage
alter table rules.rule add constraint FKmx113051ieint865l7rs8g5ao foreign key (template_id) references rules.template
alter table rules.validationmessage add constraint FK9kyla6nb7ejou8gisc1hkem8q foreign key (raw_message_id) references rules.rawmessage
create sequence rules.hibernate_sequence start 1 increment 1
create table rules.context_expression (id int8 not null, context varchar(255), expression varchar(255) not null, failure_message varchar(255), rule_id int8, primary key (id))
create table rules.messageid (id int8 not null, message_id varchar(255) not null, validation_message_id int8, primary key (id))
create table rules.rawmessage (id int8 not null, raw_message_guid varchar(255), mdc_request_id varchar(255), raw_message text not null, raw_msg_type varchar(255), primary key (id))
create table rules.rule (rule_id int8 not null, rule_created_on timestamp, br_id varchar(255) not null, disabled boolean, error_type varchar(255) not null, level varchar(255) not null, note text not null, property_names varchar(255) not null, template_id int8, primary key (rule_id))
create table rules.template (template_id int8 not null, template_name varchar(255) not null, fact_template varchar(255) not null, primary key (template_id))
create table rules.validationmessage (id int8 not null, br_id varchar(255) not null, error_type varchar(255) not null, fact_date timestamp, level varchar(255) not null, message text not null, xpath_list text, raw_message_id int8, primary key (id))
alter table rules.context_expression add constraint UKkbep01d90m2ng1iwkwmcmwyso unique (context, expression)
alter table rules.rule add constraint UK_exbochm35ucypqhkac4jj9utg unique (br_id)
alter table rules.template add constraint UK_84ekg6bq3u5gxugfdreqw37np unique (template_name)
alter table rules.template add constraint UK_ftdcn5j0igpppmkw6e6hb52tu unique (fact_template)
alter table rules.context_expression add constraint FK8vs5y07j703mcempqhodg1fmr foreign key (rule_id) references rules.rule
alter table rules.messageid add constraint FKt7r2o8i4r93s7bpobpipcfqv3 foreign key (validation_message_id) references rules.validationmessage
alter table rules.rule add constraint FKmx113051ieint865l7rs8g5ao foreign key (template_id) references rules.template
alter table rules.validationmessage add constraint FK9kyla6nb7ejou8gisc1hkem8q foreign key (raw_message_id) references rules.rawmessage
create sequence rules.hibernate_sequence start 1 increment 1
create table rules.context_expression (id int8 not null, context varchar(255), expression varchar(255) not null, failure_message varchar(255), rule_id int8, primary key (id))
create table rules.messageid (id int8 not null, message_id varchar(255) not null, validation_message_id int8, primary key (id))
create table rules.rawmessage (id int8 not null, raw_message_guid varchar(255), mdc_request_id varchar(255), raw_message text not null, raw_msg_type varchar(255), primary key (id))
create table rules.rule (rule_id int8 not null, rule_created_on timestamp, br_id varchar(255) not null, disabled boolean, error_type varchar(255) not null, level varchar(255) not null, note text not null, property_names varchar(255) not null, template_id int8, primary key (rule_id))
create table rules.template (template_id int8 not null, template_name varchar(255) not null, fact_template varchar(255) not null, primary key (template_id))
create table rules.validationmessage (id int8 not null, br_id varchar(255) not null, error_type varchar(255) not null, fact_date timestamp, level varchar(255) not null, message text not null, xpath_list text, raw_message_id int8, primary key (id))
alter table rules.context_expression add constraint UKkbep01d90m2ng1iwkwmcmwyso unique (context, expression)
alter table rules.rule add constraint UK_exbochm35ucypqhkac4jj9utg unique (br_id)
alter table rules.template add constraint UK_84ekg6bq3u5gxugfdreqw37np unique (template_name)
alter table rules.template add constraint UK_ftdcn5j0igpppmkw6e6hb52tu unique (fact_template)
alter table rules.context_expression add constraint FK8vs5y07j703mcempqhodg1fmr foreign key (rule_id) references rules.rule
alter table rules.messageid add constraint FKt7r2o8i4r93s7bpobpipcfqv3 foreign key (validation_message_id) references rules.validationmessage
alter table rules.rule add constraint FKmx113051ieint865l7rs8g5ao foreign key (template_id) references rules.template
alter table rules.validationmessage add constraint FK9kyla6nb7ejou8gisc1hkem8q foreign key (raw_message_id) references rules.rawmessage
create sequence rules.hibernate_sequence start 1 increment 1
create table rules.context_expression (id int8 not null, context varchar(255), expression varchar(255) not null, failure_message varchar(255), rule_id int8, primary key (id))
create table rules.messageid (id int8 not null, message_id varchar(255) not null, validation_message_id int8, primary key (id))
create table rules.rawmessage (id int8 not null, raw_message_guid varchar(255), mdc_request_id varchar(255), raw_message text not null, raw_msg_type varchar(255), primary key (id))
create table rules.rule (rule_id int8 not null, rule_created_on timestamp, br_id varchar(255) not null, disabled boolean, error_type varchar(255) not null, level varchar(255) not null, note text not null, property_names varchar(255) not null, template_id int8, primary key (rule_id))
create table rules.template (template_id int8 not null, template_name varchar(255) not null, fact_template varchar(255) not null, primary key (template_id))
create table rules.validationmessage (id int8 not null, br_id varchar(255) not null, error_type varchar(255) not null, fact_date timestamp, level varchar(255) not null, message text not null, xpath_list text, raw_message_id int8, primary key (id))
alter table rules.context_expression add constraint UKkbep01d90m2ng1iwkwmcmwyso unique (context, expression)
alter table rules.rule add constraint UK_exbochm35ucypqhkac4jj9utg unique (br_id)
alter table rules.template add constraint UK_84ekg6bq3u5gxugfdreqw37np unique (template_name)
alter table rules.template add constraint UK_ftdcn5j0igpppmkw6e6hb52tu unique (fact_template)
alter table rules.context_expression add constraint FK8vs5y07j703mcempqhodg1fmr foreign key (rule_id) references rules.rule
alter table rules.messageid add constraint FKt7r2o8i4r93s7bpobpipcfqv3 foreign key (validation_message_id) references rules.validationmessage
alter table rules.rule add constraint FKmx113051ieint865l7rs8g5ao foreign key (template_id) references rules.template
alter table rules.validationmessage add constraint FK9kyla6nb7ejou8gisc1hkem8q foreign key (raw_message_id) references rules.rawmessage
create sequence rules.hibernate_sequence start 1 increment 1
create table rules.context_expression (id int8 not null, context varchar(255), expression varchar(255) not null, failure_message varchar(255), rule_id int8, primary key (id))
create table rules.messageid (id int8 not null, message_id varchar(255) not null, validation_message_id int8, primary key (id))
create table rules.rawmessage (id int8 not null, raw_message_guid varchar(255), mdc_request_id varchar(255), raw_message text not null, raw_msg_type varchar(255), primary key (id))
create table rules.rule (rule_id int8 not null, rule_created_on timestamp, br_id varchar(255) not null, disabled boolean, error_type varchar(255) not null, level varchar(255) not null, note text not null, property_names varchar(255) not null, template_id int8, primary key (rule_id))
create table rules.template (template_id int8 not null, template_name varchar(255) not null, fact_template varchar(255) not null, primary key (template_id))
create table rules.validationmessage (id int8 not null, br_id varchar(255) not null, error_type varchar(255) not null, fact_date timestamp, level varchar(255) not null, message text not null, xpath_list text, raw_message_id int8, primary key (id))
alter table rules.context_expression add constraint UKkbep01d90m2ng1iwkwmcmwyso unique (context, expression)
alter table rules.rule add constraint UK_exbochm35ucypqhkac4jj9utg unique (br_id)
alter table rules.template add constraint UK_84ekg6bq3u5gxugfdreqw37np unique (template_name)
alter table rules.template add constraint UK_ftdcn5j0igpppmkw6e6hb52tu unique (fact_template)
alter table rules.context_expression add constraint FK8vs5y07j703mcempqhodg1fmr foreign key (rule_id) references rules.rule
alter table rules.messageid add constraint FKt7r2o8i4r93s7bpobpipcfqv3 foreign key (validation_message_id) references rules.validationmessage
alter table rules.rule add constraint FKmx113051ieint865l7rs8g5ao foreign key (template_id) references rules.template
alter table rules.validationmessage add constraint FK9kyla6nb7ejou8gisc1hkem8q foreign key (raw_message_id) references rules.rawmessage
create sequence rules.hibernate_sequence start 1 increment 1
create table rules.context_expression (id int8 not null, context varchar(255), expression varchar(255) not null, failure_message varchar(255), rule_id int8, primary key (id))
create table rules.messageid (id int8 not null, message_id varchar(255) not null, validation_message_id int8, primary key (id))
create table rules.rawmessage (id int8 not null, raw_message_guid varchar(255), mdc_request_id varchar(255), raw_message text not null, raw_msg_type varchar(255), primary key (id))
create table rules.rule (rule_id int8 not null, rule_created_on timestamp, br_id varchar(255) not null, disabled boolean, error_type varchar(255) not null, level varchar(255) not null, note text not null, property_names varchar(255) not null, template_id int8, primary key (rule_id))
create table rules.template (template_id int8 not null, template_name varchar(255) not null, fact_template varchar(255) not null, primary key (template_id))
create table rules.validationmessage (id int8 not null, br_id varchar(255) not null, error_type varchar(255) not null, fact_date timestamp, level varchar(255) not null, message text not null, xpath_list text, raw_message_id int8, primary key (id))
alter table rules.context_expression add constraint UKkbep01d90m2ng1iwkwmcmwyso unique (context, expression)
alter table rules.rule add constraint UK_exbochm35ucypqhkac4jj9utg unique (br_id)
alter table rules.template add constraint UK_84ekg6bq3u5gxugfdreqw37np unique (template_name)
alter table rules.template add constraint UK_ftdcn5j0igpppmkw6e6hb52tu unique (fact_template)
alter table rules.context_expression add constraint FK8vs5y07j703mcempqhodg1fmr foreign key (rule_id) references rules.rule
alter table rules.messageid add constraint FKt7r2o8i4r93s7bpobpipcfqv3 foreign key (validation_message_id) references rules.validationmessage
alter table rules.rule add constraint FKmx113051ieint865l7rs8g5ao foreign key (template_id) references rules.template
alter table rules.validationmessage add constraint FK9kyla6nb7ejou8gisc1hkem8q foreign key (raw_message_id) references rules.rawmessage
create sequence rules.hibernate_sequence start 1 increment 1
create table rules.context_expression (id int8 not null, context varchar(255), expression varchar(255) not null, failure_message varchar(255), rule_id int8, primary key (id))
create table rules.messageid (id int8 not null, message_id varchar(255) not null, validation_message_id int8, primary key (id))
create table rules.rawmessage (id int8 not null, raw_message_guid varchar(255), mdc_request_id varchar(255), raw_message text not null, raw_msg_type varchar(255), primary key (id))
create table rules.rule (rule_id int8 not null, rule_created_on timestamp, br_id varchar(255) not null, disabled boolean, error_type varchar(255) not null, level varchar(255) not null, note text not null, property_names varchar(255) not null, template_id int8, primary key (rule_id))
create table rules.template (template_id int8 not null, template_name varchar(255) not null, fact_template varchar(255) not null, primary key (template_id))
create table rules.validationmessage (id int8 not null, br_id varchar(255) not null, error_type varchar(255) not null, fact_date timestamp, level varchar(255) not null, message text not null, xpath_list text, raw_message_id int8, primary key (id))
alter table rules.context_expression add constraint UKkbep01d90m2ng1iwkwmcmwyso unique (context, expression)
alter table rules.rule add constraint UK_exbochm35ucypqhkac4jj9utg unique (br_id)
alter table rules.template add constraint UK_84ekg6bq3u5gxugfdreqw37np unique (template_name)
alter table rules.template add constraint UK_ftdcn5j0igpppmkw6e6hb52tu unique (fact_template)
alter table rules.context_expression add constraint FK8vs5y07j703mcempqhodg1fmr foreign key (rule_id) references rules.rule
alter table rules.messageid add constraint FKt7r2o8i4r93s7bpobpipcfqv3 foreign key (validation_message_id) references rules.validationmessage
alter table rules.rule add constraint FKmx113051ieint865l7rs8g5ao foreign key (template_id) references rules.template
alter table rules.validationmessage add constraint FK9kyla6nb7ejou8gisc1hkem8q foreign key (raw_message_id) references rules.rawmessage
create sequence rules.hibernate_sequence start 1 increment 1
create table rules.context_expression (id int8 not null, context varchar(255), expression varchar(255) not null, failure_message varchar(255), rule_id int8, primary key (id))
create table rules.messageid (id int8 not null, message_id varchar(255) not null, validation_message_id int8, primary key (id))
create table rules.rawmessage (id int8 not null, raw_message_guid varchar(255), mdc_request_id varchar(255), raw_message text not null, raw_msg_type varchar(255), primary key (id))
create table rules.rule (rule_id int8 not null, rule_created_on timestamp, br_id varchar(255) not null, disabled boolean, error_type varchar(255) not null, level varchar(255) not null, note text not null, property_names varchar(255) not null, template_id int8, primary key (rule_id))
create table rules.template (template_id int8 not null, template_name varchar(255) not null, fact_template varchar(255) not null, primary key (template_id))
create table rules.validationmessage (id int8 not null, br_id varchar(255) not null, error_type varchar(255) not null, fact_date timestamp, level varchar(255) not null, message text not null, xpath_list text, raw_message_id int8, primary key (id))
alter table rules.context_expression add constraint UKkbep01d90m2ng1iwkwmcmwyso unique (context, expression)
alter table rules.rule add constraint UK_exbochm35ucypqhkac4jj9utg unique (br_id)
alter table rules.template add constraint UK_84ekg6bq3u5gxugfdreqw37np unique (template_name)
alter table rules.template add constraint UK_ftdcn5j0igpppmkw6e6hb52tu unique (fact_template)
alter table rules.context_expression add constraint FK8vs5y07j703mcempqhodg1fmr foreign key (rule_id) references rules.rule
alter table rules.messageid add constraint FKt7r2o8i4r93s7bpobpipcfqv3 foreign key (validation_message_id) references rules.validationmessage
alter table rules.rule add constraint FKmx113051ieint865l7rs8g5ao foreign key (template_id) references rules.template
alter table rules.validationmessage add constraint FK9kyla6nb7ejou8gisc1hkem8q foreign key (raw_message_id) references rules.rawmessage
create sequence rules.hibernate_sequence start 1 increment 1
create table rules.context_expression (id int8 not null, context varchar(255), expression varchar(255) not null, failure_message varchar(255), rule_id int8, primary key (id))
create table rules.messageid (id int8 not null, message_id varchar(255) not null, validation_message_id int8, primary key (id))
create table rules.rawmessage (id int8 not null, raw_message_guid varchar(255), mdc_request_id varchar(255), raw_message text not null, raw_msg_type varchar(255), primary key (id))
create table rules.rule (rule_id int8 not null, rule_created_on timestamp, br_id varchar(255) not null, disabled boolean, error_type varchar(255) not null, level varchar(255) not null, note text not null, property_names varchar(255) not null, template_id int8, primary key (rule_id))
create table rules.template (template_id int8 not null, template_name varchar(255) not null, fact_template varchar(255) not null, primary key (template_id))
create table rules.validationmessage (id int8 not null, br_id varchar(255) not null, error_type varchar(255) not null, fact_date timestamp, level varchar(255) not null, message text not null, xpath_list text, raw_message_id int8, primary key (id))
alter table rules.context_expression add constraint UKkbep01d90m2ng1iwkwmcmwyso unique (context, expression)
alter table rules.rule add constraint UK_exbochm35ucypqhkac4jj9utg unique (br_id)
alter table rules.template add constraint UK_84ekg6bq3u5gxugfdreqw37np unique (template_name)
alter table rules.template add constraint UK_ftdcn5j0igpppmkw6e6hb52tu unique (fact_template)
alter table rules.context_expression add constraint FK8vs5y07j703mcempqhodg1fmr foreign key (rule_id) references rules.rule
alter table rules.messageid add constraint FKt7r2o8i4r93s7bpobpipcfqv3 foreign key (validation_message_id) references rules.validationmessage
alter table rules.rule add constraint FKmx113051ieint865l7rs8g5ao foreign key (template_id) references rules.template
alter table rules.validationmessage add constraint FK9kyla6nb7ejou8gisc1hkem8q foreign key (raw_message_id) references rules.rawmessage
create sequence rules.hibernate_sequence start 1 increment 1
create table rules.context_expression (id int8 not null, context varchar(255), expression varchar(255) not null, failure_message varchar(255), rule_id int8, primary key (id))
create table rules.messageid (id int8 not null, message_id varchar(255) not null, validation_message_id int8, primary key (id))
create table rules.rawmessage (id int8 not null, raw_message_guid varchar(255), mdc_request_id varchar(255), raw_message text not null, raw_msg_type varchar(255), primary key (id))
create table rules.rule (rule_id int8 not null, rule_created_on timestamp, br_id varchar(255) not null, disabled boolean, error_type varchar(255) not null, level varchar(255) not null, note text not null, property_names varchar(255) not null, template_id int8, primary key (rule_id))
create table rules.template (template_id int8 not null, template_name varchar(255) not null, fact_template varchar(255) not null, primary key (template_id))
create table rules.validationmessage (id int8 not null, br_id varchar(255) not null, error_type varchar(255) not null, fact_date timestamp, level varchar(255) not null, message text not null, xpath_list text, raw_message_id int8, primary key (id))
alter table rules.context_expression add constraint UKkbep01d90m2ng1iwkwmcmwyso unique (context, expression)
alter table rules.rule add constraint UK_exbochm35ucypqhkac4jj9utg unique (br_id)
alter table rules.template add constraint UK_84ekg6bq3u5gxugfdreqw37np unique (template_name)
alter table rules.template add constraint UK_ftdcn5j0igpppmkw6e6hb52tu unique (fact_template)
alter table rules.context_expression add constraint FK8vs5y07j703mcempqhodg1fmr foreign key (rule_id) references rules.rule
alter table rules.messageid add constraint FKt7r2o8i4r93s7bpobpipcfqv3 foreign key (validation_message_id) references rules.validationmessage
alter table rules.rule add constraint FKmx113051ieint865l7rs8g5ao foreign key (template_id) references rules.template
alter table rules.validationmessage add constraint FK9kyla6nb7ejou8gisc1hkem8q foreign key (raw_message_id) references rules.rawmessage
create sequence rules.hibernate_sequence start 1 increment 1
create table rules.context_expression (id int8 not null, context varchar(255), expression varchar(255) not null, failure_message varchar(255), rule_id int8, primary key (id))
create table rules.messageid (id int8 not null, message_id varchar(255) not null, validation_message_id int8, primary key (id))
create table rules.rawmessage (id int8 not null, raw_message_guid varchar(255), mdc_request_id varchar(255), raw_message text not null, raw_msg_type varchar(255), primary key (id))
create table rules.rule (rule_id int8 not null, rule_created_on timestamp, br_id varchar(255) not null, disabled boolean, error_type varchar(255) not null, level varchar(255) not null, note text not null, property_names varchar(255) not null, template_id int8, primary key (rule_id))
create table rules.template (template_id int8 not null, template_name varchar(255) not null, fact_template varchar(255) not null, primary key (template_id))
create table rules.validationmessage (id int8 not null, br_id varchar(255) not null, error_type varchar(255) not null, fact_date timestamp, level varchar(255) not null, message text not null, xpath_list text, raw_message_id int8, primary key (id))
alter table rules.context_expression add constraint UKkbep01d90m2ng1iwkwmcmwyso unique (context, expression)
alter table rules.rule add constraint UK_exbochm35ucypqhkac4jj9utg unique (br_id)
alter table rules.template add constraint UK_84ekg6bq3u5gxugfdreqw37np unique (template_name)
alter table rules.template add constraint UK_ftdcn5j0igpppmkw6e6hb52tu unique (fact_template)
alter table rules.context_expression add constraint FK8vs5y07j703mcempqhodg1fmr foreign key (rule_id) references rules.rule
alter table rules.messageid add constraint FKt7r2o8i4r93s7bpobpipcfqv3 foreign key (validation_message_id) references rules.validationmessage
alter table rules.rule add constraint FKmx113051ieint865l7rs8g5ao foreign key (template_id) references rules.template
alter table rules.validationmessage add constraint FK9kyla6nb7ejou8gisc1hkem8q foreign key (raw_message_id) references rules.rawmessage
