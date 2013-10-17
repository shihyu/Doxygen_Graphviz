#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace facade
			{
				namespace example1
				{
					///
					/// <summary> * ʾ����������������Model����ʵ���������ݻ�ܶ� </summary>
					/// 
					class ConfigModel
					{
					///	
					///	 <summary> * �Ƿ���Ҫ���ɱ��ֲ㣬Ĭ����true </summary>
					///	 
					private:
						bool needGenPresentation;
					///	
					///	 <summary> * �Ƿ���Ҫ�����߼��㣬Ĭ����true </summary>
					///	 
						bool needGenBusiness;
					///	
					///	 <summary> * �Ƿ���Ҫ����DAO��Ĭ����true </summary>
					///	 
						bool needGenDAO;
					public:
						virtual bool isNeedGenPresentation();
						virtual void setNeedGenPresentation(bool needGenPresentation);
						virtual bool isNeedGenBusiness();
						virtual void setNeedGenBusiness(bool needGenBusiness);
						virtual bool isNeedGenDAO();
						virtual void setNeedGenDAO(bool needGenDAO);

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								needGenPresentation = true;
								needGenBusiness = true;
								needGenDAO = true;

								initialized = true;
							}
						}

public:
	ConfigModel()
	{
		InitializeInstanceFields();
	}
					};

				}
			}
		}
	}
}