#pragma once

#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace singleton
			{
				namespace example7
				{
					///
					/// <summary> * Java�л���Ļ���ʵ��ʾ�� </summary>
					/// 
					class JavaCache
					{
					///	
					///	 <summary> * �������ݵ������������Map�Ƿ�����ʣ�ֱ�Ӹ���Key�Ϳ��Ի�ȡValue��
					///	 * keyѡ��String��Ϊ�˼򵥣�������ʾ </summary>
					///	 
					private:
						Map<std::string, object*> *map;
					///	
					///	 <summary> * �ӻ����л�ȡֵ </summary>
					///	 * <param name="key"> ����ʱ���keyֵ </param>
					///	 * <returns> key��Ӧ��Valueֵ </returns>
					///	 
					public:
						virtual object *getValue(std::string key);

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								map = std::map<std::string, object*>();

								initialized = true;
							}
						}

public:
	JavaCache()
	{
		InitializeInstanceFields();
	}
					};

				}
			}
		}
	}
}