#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace state
			{
				namespace example2
				{


					///
					/// <summary> * ��װ��Context��һ���ض�״̬��ص���Ϊ </summary>
					/// 
					class State
					{
					///	
					///	 <summary> * ״̬��Ӧ�Ĵ��� </summary>
					///	 * <param name="sampleParameter"> ʾ��������˵�����Դ�����������崫��
					///	 *             ʲô���Ĳ��������뼸���������ɾ���Ӧ����������� </param>
					///	 
					public:
						virtual public void handle(std::string) = 0;
					};
				}
			}
		}
	}
}