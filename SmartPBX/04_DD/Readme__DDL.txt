=================================================
  DDL�ɂ���
=================================================
�쐬���ƃu�����`����t�@�C�����ȉ��Ɏ����B


���쐬��

ALTER_TABLE_StepX.X.sql
  ������Step�ŕύX���������"����"���L��
    DB�\���ɕύX���Ȃ��ꍇ�͍쐬�s�v�B
    ��Step��erm�t�@�C������A��Step�̕ύX���𔲂��o���B
    �iWinMerge�őOStep��CREATE_TABLE�ƍ�Step��CREATE_TABLE��diff�Ŋm�F����Ɨǂ��B�j
    ���u�����`����
    DB�ύX���ɐV�K�쐬����t�@�C���Ȃ̂Ńu�����`���Ȃ�
    �i�O��Step����R�s�[�͕s�v�j

CREATE_TABLE_StepX.[X-1].sql
  ���OStep��DDL (���j�[�N�C���f�b�N�X������)
    ��ErMaster����G�N�X�|�[�g��������
    ���u�����`����
    �O��Step��erm�t�@�C������o�͂������̂�z�u
    �i�O��Step����R�s�[�͕s�v�j

CREATE_UNIQUE_INDEX_StepX.[X-1].sql
  �����j�[�N�C���f�b�N�X��ALTER TABLE�����L�ڂ������́B
    ��ErMaster�̃G�N�X�|�[�g���̓��j�[�N�C���f�b�N�X�̍\�����Ӑ}�ʂ�o�͂���Ȃ����߁A���̃t�@�C���ɕʓr�Ǘ����Ă���B
    ���u�����`����
    �OStep�́uCREATE_UNIQUE_INDEX�v����u�����`�B(�R�s�[)
    ���j�[�N�C���f�b�N�X�ɕύX���Ȃ�������̓u�����`�����܂܂ŗǂ��B
    �ύX������ꍇ�͎��ƂŏC������B

=================================================

����Step��DB��1������ꍇ(�f�[�^0��)
�ȉ��̏��Ԃ�DDL��S�Ď��{�B
  CREATE_TABLE_StepX.[X-1].sql
  CREATE_UNIQUE_INDEX_StepX.[X-1].sql
  ALTER_TABLE_StepX.X.sql

���O��Step��DB���g���񂷏ꍇ
�O��Step��DB�ɑ΂��AALTER_TABLE_StepX.X.sql�����{�B
�i2�ȏ�O��Step�̏ꍇ�A�Ԃ�Step��ALTER_TABLE�̎��{���K�v�j

=================================================

