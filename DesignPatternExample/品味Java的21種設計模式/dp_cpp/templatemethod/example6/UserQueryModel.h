#pragma once

#include "UserModel.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace templatemethod
			{
				namespace example6
				{
					///
					/// <summary> * ������ѯ�û����������ݵ�ģ�� </summary>
					/// 
					class UserQueryModel : public UserModel
					{
					///	
					///	 <summary> * ������һ�������ѯ��Ҳ���������ѯ�����������ǣ�
					///	 * age >= ����ֵ1  and  age <= ����ֵ2 
					///	 * ��UserModel�е�age��������ֵ1��
					///	 * ���ﶨ���age2��������ֵ2 </summary>
					///	 
					private:
						int age2;

					public:
						virtual int getAge2();

						virtual void setAge2(int age2);
					};

				}
			}
		}
	}
}