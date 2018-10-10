alter table rules.messageid drop constraint FK_sfsxbqls9pm1i30d34n51nndg
alter table rules.rule drop constraint FK_cqshh8msujtb44r02dbxm4edt
alter table rules.validationmessage drop constraint FK_iluwhraqjw4vxtq1jgte44a8p
drop table if exists rules.messageid cascade
drop table if exists rules.rawmessage cascade
drop table if exists rules.rule cascade
drop table if exists rules.template cascade
drop table if exists rules.validationmessage cascade
drop sequence rules.hibernate_sequence
