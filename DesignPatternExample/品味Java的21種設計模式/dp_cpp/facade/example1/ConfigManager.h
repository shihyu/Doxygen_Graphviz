#pragma once

#include "ConfigModel.h"

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
					/// <summary> * ʾ�����ù������Ǹ����ȡ�����ļ���
					/// * ���������ļ����������õ�����Model��ȥ���Ǹ����� </summary>
					/// 
					class ConfigManager
					{
					private:
						static ConfigManager *manager;
						static ConfigModel *cm;
						ConfigManager();
					public:
						static ConfigManager *getInstance();
					///	
					///	 <summary> * ��ȡ���õ����� </summary>
					///	 * <returns> ���õ����� </returns>
					///	 
						virtual ConfigModel *getConfigData();
					};

				}
			}
		}
	}
}